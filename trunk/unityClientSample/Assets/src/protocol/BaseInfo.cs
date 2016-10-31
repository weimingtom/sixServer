using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
	[ProtoContract]
	public class BaseInfo
	{
        /// <summary>
        ///  基地id
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
        ///  消耗
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public int cost;
        /// <summary>
        ///  科技等级
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public int science;
        /// <summary>
        ///  技能
        /// </summary>
		[ProtoMember(6, IsRequired = false)]
		public string skill;
        /// <summary>
        ///  美术资源
        /// </summary>
		[ProtoMember(7, IsRequired = false)]
		public string art;
        /// <summary>
        ///  描述
        /// </summary>
		[ProtoMember(8, IsRequired = false)]
		public string desc;

	}
}