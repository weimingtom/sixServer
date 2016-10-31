using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// base lv 协议:22
    /// </summary>
	[Proto(value=22,description="base lv")]
	[ProtoContract]
	public class BaseLvRequest
	{
        /// <summary>
        ///  roomId
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string roomId;

	}
}