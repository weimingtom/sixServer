using ProtoBuf;
using System.Collections.Generic;
using System.ComponentModel;
using System;
using System.Runtime.Serialization;

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