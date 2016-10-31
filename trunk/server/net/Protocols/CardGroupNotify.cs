using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 套牌推送 协议:-30
    /// </summary>
	[Proto(value=-30,description="套牌推送")]
	[ProtoContract]
	public class CardGroupNotify
	{
        /// <summary>
        ///  玩家id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  套牌组
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public List<CardGroupInfo> cardGroup;
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