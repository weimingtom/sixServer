using ProtoBuf;
using System.Collections.Generic;
using System;

namespace protocol
{
    /// <summary>
    /// 战斗结果 协议:-32
    /// </summary>
	[Proto(value=-32,description="战斗结果")]
	[ProtoContract]
	public class BattleOverNotify
	{
        /// <summary>
        ///  房间id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string roomId;
        /// <summary>
        ///  胜利true 失败false
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public bool flag;
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