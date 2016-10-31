using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
	[ProtoContract]
	public class CardInfo
	{
        /// <summary>
        ///  id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string id;
        /// <summary>
        ///  名称
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string name;
        /// <summary>
        ///  血量
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public int blood;
        /// <summary>
        ///  攻击
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public int att;
        /// <summary>
        ///  种族
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public int race;
        /// <summary>
        ///  类型
        /// </summary>
		[ProtoMember(6, IsRequired = false)]
		public int type;
        /// <summary>
        ///  对应技能
        /// </summary>
		[ProtoMember(7, IsRequired = false)]
		public List<string> skill;
        /// <summary>
        ///  需要科技
        /// </summary>
		[ProtoMember(8, IsRequired = false)]
		public int science;
        /// <summary>
        ///  消耗
        /// </summary>
		[ProtoMember(9, IsRequired = false)]
		public int cost;
        /// <summary>
        ///  状态
        /// </summary>
		[ProtoMember(10, IsRequired = false)]
		public int status;
        /// <summary>
        ///  行动力
        /// </summary>
		[ProtoMember(11, IsRequired = false)]
		public int action;
        /// <summary>
        ///  buff
        /// </summary>
		[ProtoMember(12, IsRequired = false)]
		public List<string> buff;
        /// <summary>
        ///  美术资源
        /// </summary>
		[ProtoMember(13, IsRequired = false)]
		public string art;

	}
}