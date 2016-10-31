using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 卡牌行动 协议:-23
    /// </summary>
	[Proto(value=-23,description="卡牌行动")]
	[ProtoContract]
	public class ActionResponse
	{
        /// <summary>
        ///  用户id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  卡牌id
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string uniqueId;
        /// <summary>
        ///  目标
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public string targetId;
        /// <summary>
        ///  技能
        /// </summary>
		[ProtoMember(6, IsRequired = false)]
		public List<SkillInfo> skill;
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