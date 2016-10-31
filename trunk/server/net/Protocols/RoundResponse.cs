using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// roundOver 协议:-25
    /// </summary>
	[Proto(value=-25,description="roundOver")]
	[ProtoContract]
	public class RoundResponse
	{
        /// <summary>
        ///  房间id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string roomId;
        /// <summary>
        ///  回合信息
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public RoundInfo nri;
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