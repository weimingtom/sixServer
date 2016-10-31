using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// connector上的验证请求 协议:6
    /// </summary>
	[Proto(value=6,description="connector上的验证请求")]
	[ProtoContract]
	public class AuthRequest
	{
        /// <summary>
        ///  唯一id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public int ucenterId;
        /// <summary>
        ///  用户名
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string userCode;
        /// <summary>
        ///  是否是测试号
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public bool testing;
        /// <summary>
        ///  登陆的服务器id
        /// </summary>
		[ProtoMember(4, IsRequired = true)]
		public int serverid;

	}
}