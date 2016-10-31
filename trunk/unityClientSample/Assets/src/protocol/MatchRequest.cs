using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// match 协议:20
    /// </summary>
	[Proto(value=20,description="match")]
	[ProtoContract]
	public class MatchRequest
	{
        /// <summary>
        ///  select the card group
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string cardGroupId;

	}
}