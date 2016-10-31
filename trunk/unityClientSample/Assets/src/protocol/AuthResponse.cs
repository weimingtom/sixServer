using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 登陆返回 协议:-6
    /// </summary>
	[Proto(value=-6,description="登陆返回")]
	[ProtoContract]
	public class AuthResponse
	{
        /// <summary>
        ///  用户的id==null
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string pid;
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