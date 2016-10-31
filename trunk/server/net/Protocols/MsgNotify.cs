using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 飘字 协议:-801
    /// </summary>
	[Proto(value=-801,description="飘字")]
	[ProtoContract]
	public class MsgNotify
	{
        /// <summary>
        ///  消息类型
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public EMsg type;
        /// <summary>
        ///  消息
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string msg;
        /// <summary>
        ///  是否成功
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public bool success;
        /// <summary>
        ///  错误消息
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string info;

	}
}