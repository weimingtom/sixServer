using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
	[ProtoContract]
	public class CardGroupInfo
	{
        /// <summary>
        ///  id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string id;
        /// <summary>
        ///  属于哪个玩家
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  卡组名称
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string name;
        /// <summary>
        ///  基地
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string baseInitId;
        /// <summary>
        ///  卡牌组
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public List<string> cardIds;

	}
}