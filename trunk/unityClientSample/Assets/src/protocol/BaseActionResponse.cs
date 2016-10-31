using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 基地行动 协议:-24
    /// </summary>
	[Proto(value=-24,description="基地行动")]
	[ProtoContract]
	public class BaseActionResponse
	{
        /// <summary>
        ///  用户id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  技能
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
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