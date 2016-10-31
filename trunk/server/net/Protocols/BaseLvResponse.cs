using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 基地升级 协议:-22
    /// </summary>
	[Proto(value=-22,description="基地升级")]
	[ProtoContract]
	public class BaseLvResponse
	{
        /// <summary>
        ///  玩家id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  资源数
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public int resource;
        /// <summary>
        ///  base lv
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public int baseLv;
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