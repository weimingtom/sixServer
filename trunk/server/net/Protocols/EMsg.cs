using ProtoBuf;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.ComponentModel;
using System;

namespace protocol
{
	[ProtoContract]
	public enum EMsg
	{
        /// <summary>
        ///  弹框
        /// </summary>
		[EnumMember]
		[ProtoEnum]
		[Description("弹框")]
		Form=1,
        /// <summary>
        ///  飘字
        /// </summary>
		[EnumMember]
		[ProtoEnum]
		[Description("飘字")]
		Fly=2,
        /// <summary>
        ///  跑马灯
        /// </summary>
		[EnumMember]
		[ProtoEnum]
		[Description("跑马灯")]
		Marquee=3

	}
}