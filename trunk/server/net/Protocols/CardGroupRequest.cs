using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 添加套牌 协议:8
    /// </summary>
	[Proto(value=8,description="添加套牌")]
	[ProtoContract]
	public class CardGroupRequest
	{
        /// <summary>
        ///  套牌id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string id;
        /// <summary>
        ///  name
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string name;
        /// <summary>
        ///  基地id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string baseInitId;
        /// <summary>
        ///  卡牌ids
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public List<string> cardIds;

	}
}