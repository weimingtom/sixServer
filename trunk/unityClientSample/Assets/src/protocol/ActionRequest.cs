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
        ///  出牌索引
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public int launch;
        /// <summary>
        ///  出牌id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string cardId;
        /// <summary>
        ///  我方:0 敌方:1
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public int side;
        /// <summary>
        ///  目标
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public int target;

	}
}