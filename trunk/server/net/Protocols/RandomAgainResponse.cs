using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 回合开始选择随机手牌 协议:-26
    /// </summary>
	[Proto(value=-26,description="回合开始选择随机手牌")]
	[ProtoContract]
	public class RandomAgainResponse
	{
        /// <summary>
        ///  roomId
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string roomId;
        /// <summary>
        ///  pid
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  手牌信息
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public List<CardInfo> hand;
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