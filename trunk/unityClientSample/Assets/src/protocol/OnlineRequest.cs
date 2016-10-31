using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 这是一条服务器的转发 协议:9999
    /// </summary>
	[Proto(value=9999,description="这是一条服务器的转发")]
	[ProtoContract]
	public class OnlineRequest
	{
		[ProtoMember(1, IsRequired = false)]
		public string pid;

	}
}