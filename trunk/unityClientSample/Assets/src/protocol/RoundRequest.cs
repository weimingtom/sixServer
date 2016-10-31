using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// roundOver 协议:25
    /// </summary>
	[Proto(value=25,description="roundOver")]
	[ProtoContract]
	public class RoundRequest
	{
        /// <summary>
        ///  roomId
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string roomId;

	}
}