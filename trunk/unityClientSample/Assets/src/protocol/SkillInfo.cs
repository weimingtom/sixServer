using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
	[ProtoContract]
	public class SkillInfo
	{
        /// <summary>
        ///  技能id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string id;
        /// <summary>
        ///  受影响的卡牌id
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public List<string> target;

	}
}