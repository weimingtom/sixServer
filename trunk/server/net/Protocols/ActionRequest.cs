using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 卡牌行动 协议:23
    /// </summary>
	[Proto(value=23,description="卡牌行动")]
	[ProtoContract]
	public class ActionRequest
	{
        /// <summary>
        ///  房间id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string roomId;
        /// <summary>
        ///  出牌id
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string uniqueId;
        /// <summary>
        ///  目标
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string targetId;

	}
}