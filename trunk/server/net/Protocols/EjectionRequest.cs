using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 出牌 协议:21
    /// </summary>
	[Proto(value=21,description="出牌")]
	[ProtoContract]
	public class EjectionRequest
	{
        /// <summary>
        ///  房间id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string roomId;
        /// <summary>
        ///  出牌id
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string uniqueId;
        /// <summary>
        ///  摆放位置
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public int position;
        /// <summary>
        ///  目标
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string targetId;

	}
}