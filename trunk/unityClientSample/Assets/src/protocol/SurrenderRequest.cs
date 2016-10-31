using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 我选择死亡 协议:27
    /// </summary>
	[Proto(value=27,description="我选择死亡")]
	[ProtoContract]
	public class SurrenderRequest
	{
        /// <summary>
        ///  roomId
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string roomId;

	}
}