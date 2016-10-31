using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 回合开始选择随机手牌 协议:26
    /// </summary>
	[Proto(value=26,description="回合开始选择随机手牌")]
	[ProtoContract]
	public class RandomAgainRequest
	{
        /// <summary>
        ///  房间id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string roomId;
        /// <summary>
        ///  要随机掉的牌
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public List<int> indexs;

	}
}