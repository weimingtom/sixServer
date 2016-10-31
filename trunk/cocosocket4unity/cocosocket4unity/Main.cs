using System;
using LitJson;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Sockets;
using System.Net;
using protocol;
using System.IO;
//引用的
using System.Reflection;
namespace cocosocket4unity
{
    class MainClass
    {
        public static void Main(string[] args)
        {
            /**
            AuthRequest ar = new AuthRequest();
            ar.serverid = 1;
            ar.userCode = "zs";
            ar.testing = false;
            ar.ucenterId = 1;
            string json = JsonMapper.ToJson(ar);
            Console.WriteLine(ar.GetType().Name + ":" + json);
            List<Type> ls = ClassUtil.GetClasses("protocol");
            foreach (Type item in ls)
            {
                Console.WriteLine(item.Name);
                ConstructorInfo constructor = item.GetConstructor(new Type[0]);
                //使用构造器对象来创建对象
                object obj = constructor.Invoke(new Object[0]);

                ProtoAttribute arr = (ProtoAttribute)ClassUtil.GetAttribute(item, typeof(ProtoAttribute));
                if (arr != null)
                    Console.WriteLine(arr.value);
            }
            Console.Read();
            **/
            /**
           long time_JAVA_Long = 1446050129676L;//java长整型日期，毫秒为单位             
           DateTime dt_1970 = new DateTime(1970, 1, 1, 0, 0, 0);              
           long tricks_1970 = dt_1970.Ticks;//1970年1月1                 
           long time_tricks = tricks_1970 + time_JAVA_Long * 10000;//日志日期刻度    
           DateTime dt = new DateTime(time_tricks).AddHours(8);//转化为DateTime
           Console.WriteLine(string.Format("{0:G}", dt));
           Console.Read();
           */
            SocketListner listner = new TestListner();
            USocket us = new USocket();
            us.setLister(listner);
            //Protocal p = new Varint32HeaderProtocol ();
            Protocal p = new LVProtocal();
            us.setProtocal(p);
            us.Connect("119.29.189.247", 4887);
            //us.Connect("127.0.0.1", 4887);



            /*
            MemoryStream stream = new MemoryStream();
            ProtoBuf.Serializer.NonGeneric.Serialize(stream, ar);
            byte[] bs = stream.ToArray();

            Frame f = new Frame(512);
            f.PutShort(6);
            Frame.xor(bs, System.Text.Encoding.UTF8.GetBytes("421w6tW1ivg="));

            f.PutBytes(bs);
            f.End();
            us.Send(f);
            **/
            Console.Read();
        }
    }
}
