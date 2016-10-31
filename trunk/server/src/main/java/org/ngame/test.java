package org.ngame;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bouncycastle.util.encoders.UrlBase64;
import org.ngame.data.DataBuilder;
import org.ngame.data.init.D_Card;
import org.ngame.protocol.annotation.Proto;
import org.ngame.protocol.domain.CardInfo;
import org.ngame.util.DESMD5;

import com.alibaba.fastjson.JSONObject;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.utils.FieldInfo;
import com.baidu.bjf.remoting.protobuf.utils.FieldUtils;
import com.baidu.bjf.remoting.protobuf.utils.ProtobufProxyUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class test {
	public static void main(String[] args) throws UnsupportedEncodingException, Exception {
//		byte[] a={62, 48, 2, 68, 38, 88};
//		printHexString(a);
		Spring.init();
		D_Card dc=DataBuilder.build(new D_Card("10101"));
		System.out.println(dc.toString());
//		CardInfo ci=new CardInfo();
//		ci.change(dc);
//		D_Card dc=new D_Card("1");
//		D_Card dc1=new D_Card("1");
//		System.out.println(dc.equals(dc1));
//		System.out.println(dc.hashCode()==dc1.hashCode());
//		System.out.println(dc.hashCode());
//		System.out.println(dc.hashCode());
//		dc.setArt("12321");
//		dc.setId("12321");
//		System.out.println(dc.hashCode());
//		List<D_Card> list=new ArrayList<>();
//		List<D_Card> list1=new ArrayList<>();
//		list.add(dc);
//		D_Card dlone=(D_Card) dc.clone();
//		dlone.setArt("00009");s
//		list1.add(dc1);
//		boolean flag=list1.removeAll(list);
//		System.out.println(flag+"\t"+list1.size());
		
		//TODO ..生成loginId
//		String desKey="421w6tW1ivg=";
//		String loginId=getLoginId(desKey, 1L, "csf", true);
//		System.out.println(loginId);
//		JSONObject json = JSON.parseObject(new String(DESMD5.decrypt(UrlBase64.decode(loginId.getBytes("UTF-8")), desKey)));
//		System.out.println(json);
		
//		ByteBuf bb=PooledByteBufAllocator.DEFAULT.buffer(512);
//		bb.writeByte(1);
//		System.out.println("ceshi");
		List<Integer> list=new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(7);
		List<Integer> list1=new ArrayList<Integer>();
		list1.add(list.get(1));
		list1.add(list.get(2));
		 Collections.shuffle(list1);
		 for(Integer i : list){
			 System.out.println(i);
		 }
	}
	
	 public static void printHexString(byte[] b) { 
		  for (int i = 0; i < b.length; i++) { 
		  String hex = Integer.toHexString(b[i] & 0xFF); 
		  if (hex.length() == 1) { 
		  hex = '0' + hex; 
		  } 
		  System.out.print("0x"+hex.toUpperCase()+","); 
		  } 

		  }
	 
	 
	 /**
	   * 生成一个loginId
	   * @param desKey
	   * @return
	   */
	  public static String getLoginId(String desKey,Long id,String usercode,boolean testing)
	  {
	    try
	    {
	      JSONObject json = new JSONObject();
	      json.put("id", id);
	      json.put("usercode", usercode);
	      json.put("testing", testing);
	      String s = new String(UrlBase64.encode(DESMD5.encrypt(json.toString().getBytes("UTF-8"), desKey)), "UTF-8");
	      return s;
	    } catch (Exception e)
	    {
	      e.printStackTrace();
	    }
	    return null;
	  }
	  
	  
	  
	  
	  public static void generateIDL(StringBuilder code, Class<?> cls, Set<Class<?>> cachedTypes, Set<Class<?>> cachedEnumTypes)
	  {
	    List<Field> fields = FieldUtils.findMatchedFields(cls, Protobuf.class);
	    Set<Class<?>> subTypes = new HashSet<>();
	    Set<Class<Enum>> enumTypes = new HashSet<>();
	    Proto p=cls.getAnnotation(Proto.class);
	    if(p!=null){
	    	 code.append(cls.getSimpleName()).append("Id = "+p.value()+"\n");
	    	 code.append("protocolInfos["+p.value()+"] = "+"{strings.TestView.protoPath},\"protocol."+cls.getSimpleName());
	    if (subTypes.isEmpty())
	    {
	      return;
	    }
	    for (Class<?> subType : subTypes)
	    {
	      generateIDL(code, subType, cachedTypes, cachedEnumTypes);
	    }
	  }
	}
	
}
