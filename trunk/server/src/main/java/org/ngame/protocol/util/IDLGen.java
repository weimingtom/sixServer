/**
 * 生成idl文件
 */
package org.ngame.protocol.util;

import com.baidu.bjf.remoting.protobuf.EnumReadable;
import com.baidu.bjf.remoting.protobuf.FieldType;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ngame.protocol.annotation.Proto;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.utils.FieldInfo;
import com.baidu.bjf.remoting.protobuf.utils.FieldUtils;
import com.baidu.bjf.remoting.protobuf.utils.ProtobufProxyUtils;

/**
 * 生成idl
 */
public class IDLGen
{

  private static final Set<Class<?>> cachedTypes = new HashSet<>();
  private static final Set<Class<?>> cachedEnumTypes = new HashSet<>();

  /**
   * 生成
   *
   * @param cls
   * @return
   */
  public static String idl(Class<?> cls)
  {
    //cachedTypes.add(cls);
    return getIDL(cls, cachedTypes, cachedEnumTypes, true);
  }

  public static String getIDL(final Class<?> cls, final Set<Class<?>> cachedTypes, final Set<Class<?>> cachedEnumTypes, boolean ignoreJava)
  {
    Set<Class<?>> types = cachedTypes;
    if (types == null)
    {
      types = new HashSet<>();
    }
    Set<Class<?>> enumTypes = cachedEnumTypes;
    if (enumTypes == null)
    {
      enumTypes = new HashSet<>();
    }
    if (types.contains(cls))
    {
      return null;
    }
    StringBuilder code = new StringBuilder();
    if (!ignoreJava)
    {
      // define package
      code.append("package ").append(cls.getPackage().getName()).append(";\n");
      code.append("option java_outer_classname = \"").append(cls.getSimpleName()).append("$$ByJProtobuf\";\n");
    }
    // define outer name class
    types.add(cls);
    generateIDL(code, cls, types, enumTypes);
    return code.toString();
  }

  private static void generateIDL(StringBuilder code, Class<?> cls, Set<Class<?>> cachedTypes, Set<Class<?>> cachedEnumTypes)
  {
    List<Field> fields = FieldUtils.findMatchedFields(cls, Protobuf.class);
    Set<Class<?>> subTypes = new HashSet<>();
    Set<Class<Enum>> enumTypes = new HashSet<>();
    code.append("message ").append(cls.getSimpleName()).append(" {  \n");
    List<FieldInfo> fieldInfos = ProtobufProxyUtils.processDefaultValue(fields);
    //TODO ....添加message cmd
//    Proto p=cls.getAnnotation(Proto.class);
//    if(p!=null)  code.append("required int32 cmd="+cmd+";\n");
    for (FieldInfo field : fieldInfos)
    {
      if (field.getFieldType() == FieldType.OBJECT)
      {
        if (field.isList())
        {
          Type type = field.getField().getGenericType();
          if (type instanceof ParameterizedType)
          {
            ParameterizedType ptype = (ParameterizedType) type;
            Type[] actualTypeArguments = ptype.getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0)
            {
              Type targetType = actualTypeArguments[0];
              if (targetType instanceof Class)
              {
                Class c = (Class) targetType;
                String fieldTypeName;
                if (ProtobufProxyUtils.isScalarType(c))
                {
                  FieldType fieldType = ProtobufProxyUtils.TYPE_MAPPING.get(c);
                  fieldTypeName = fieldType.getType();
                } else
                {
                  if (!cachedTypes.contains(c))
                  {
                    cachedTypes.add(c);
                    subTypes.add(c);
                  }
                  fieldTypeName = c.getSimpleName();
                }
                code.append("repeated ").append(fieldTypeName).append(" ")
                        .append(field.getField().getName()).append("=").append(field.getOrder())
                        .append(";");
              }
            }
          }
        } else
        {
          Class c = field.getField().getType();
          code.append(getFieldRequired(field.isRequired())).append(" ").append(c.getSimpleName()).append(" ")
                  .append(field.getField().getName()).append("=").append(field.getOrder()).append(";");
          if (!cachedTypes.contains(c))
          {
            cachedTypes.add(c);
            subTypes.add(c);
          }
        }
      } else
      {
        String type = field.getFieldType().getType().toLowerCase();
        if (field.getFieldType() == FieldType.ENUM)
        {
          Class c = field.getField().getType();
          if (Enum.class.isAssignableFrom(c))
          {
            type = c.getSimpleName();
            if (!cachedEnumTypes.contains(c))
            {
              cachedEnumTypes.add(c);
              enumTypes.add(c);
            }
          }
        }
        String required = getFieldRequired(field.isRequired());
        if (field.isList())
        {
          required = "repeated";
        }
        code.append(required).append(" ").append(type).append(" ").append(field.getField().getName())
                .append("=").append(field.getOrder()).append(";");
      }
      if (field.hasDescription())
      {
        code.append(" // ").append(field.getDescription()).append("\n");
      } else
      {
        code.append("\n");
      }
    }
    code.append("}\n");
    for (Class<Enum> subType : enumTypes)
    {
      generateEnumIDL(code, subType);
    }
    if (subTypes.isEmpty())
    {
      return;
    }
    for (Class<?> subType : subTypes)
    {
      generateIDL(code, subType, cachedTypes, cachedEnumTypes);
    }
  }

  private static String getFieldRequired(boolean required)
  {
    if (required)
    {
      return "required";
    }
    return "optional";
  }

  private static void generateEnumIDL(StringBuilder code, Class<Enum> cls)
  {
    code.append("enum ").append(cls.getSimpleName()).append(" {  \n");
    Field[] fields = cls.getFields();
    for (Field field : fields)
    {
      String name = field.getName();
      code.append(name).append("=");
      Enum value = Enum.valueOf(cls, name);
      if (value instanceof EnumReadable)
      {
        code.append(((EnumReadable) value).value());
      } else
      {
        code.append(value.ordinal());
      }
      code.append(";");
      code.append(" // ").append(value.toString()).append("\n");
    }
    code.append("}\n ");
  }
}
