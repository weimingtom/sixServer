using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
	[ProtoContract]
	public class RoundInfo
	{
        /// <summary>
        ///  攻击方id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string attId;
        /// <summary>
        ///  回合数
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public int round;
        /// <summary>
        ///  资源数
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public int resouce;
        /// <summary>
        ///  新抓到的卡牌
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public List<CardInfo> add;
        /// <summary>
        ///  需要加行动力的卡牌id,攻击方
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public List<string> action;
        /// <summary>
        ///  需要移除buff的卡牌id,防守方
        /// </summary>
		[ProtoMember(6, IsRequired = false)]
		public List<string> remove;

	}
}