using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// base action 协议:24
    /// </summary>
	[Proto(value=24,description="base action")]
	[ProtoContract]
	public class BaseActionRequest
	{
        /// <summary>
        ///  roomId
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string roomId;
        /// <summary>
        ///  target
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string targetId;

	}
}