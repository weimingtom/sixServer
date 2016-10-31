using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 出牌 协议:-21
    /// </summary>
	[Proto(value=-21,description="出牌")]
	[ProtoContract]
	public class EjectionResponse
	{
        /// <summary>
        ///  用户id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  卡牌唯一id
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string uniqueId;
        /// <summary>
        ///  卡牌id
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public string cardId;
        /// <summary>
        ///  摆放位置
        /// </summary>
		[ProtoMember(6, IsRequired = false)]
		public int position;
        /// <summary>
        ///  目标
        /// </summary>
		[ProtoMember(7, IsRequired = false)]
		public int target;
        /// <summary>
        ///  技能
        /// </summary>
		[ProtoMember(8, IsRequired = false)]
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