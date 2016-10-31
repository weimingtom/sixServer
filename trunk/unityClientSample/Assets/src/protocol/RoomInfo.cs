using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
	[ProtoContract]
	public class RoomInfo
	{
        /// <summary>
        ///  房间id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string id;
        /// <summary>
        ///  场景icon
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string background;
        /// <summary>
        ///  我方信息
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public BattleInfo attInfo;
        /// <summary>
        ///  敌方信息
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public BattleInfo defInfo;

	}
}