using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
	[ProtoContract]
	public class BattleInfo
	{
        /// <summary>
        ///  玩家id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  玩家昵称
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string name;
        /// <summary>
        ///  玩家等级
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public int lv;
        /// <summary>
        ///  玩家头像
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string icon;
        /// <summary>
        ///  基地信息
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public BaseInfo baseInfo;
        /// <summary>
        ///  当前回合数
        /// </summary>
		[ProtoMember(6, IsRequired = false)]
		public int round;
        /// <summary>
        ///  资源
        /// </summary>
		[ProtoMember(7, IsRequired = false)]
		public int resource;
        /// <summary>
        ///  手牌
        /// </summary>
		[ProtoMember(8, IsRequired = false)]
		public List<CardInfo> handCard;
        /// <summary>
        ///  桌子上的牌
        /// </summary>
		[ProtoMember(9, IsRequired = false)]
		public List<CardInfo> tableCard;
        /// <summary>
        ///  牌堆里的牌
        /// </summary>
		[ProtoMember(10, IsRequired = false)]
		public List<CardInfo> heapCard;
        /// <summary>
        ///  已使用的牌
        /// </summary>
		[ProtoMember(11, IsRequired = false)]
		public List<CardInfo> outCard;

	}
}