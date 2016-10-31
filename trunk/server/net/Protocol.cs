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

    /// <summary>
    /// ping协议，保活 协议:7
    /// </summary>
	[Proto(value=7,description="ping协议，保活")]
	[ProtoContract]
	public class Ping
	{
        /// <summary>
        ///  客户端的时间
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public long time;

	}

    /// <summary>
    /// ping协议的返回 协议:-7
    /// </summary>
	[Proto(value=-7,description="ping协议的返回")]
	[ProtoContract]
	public class Pong
	{
        /// <summary>
        ///  服务器的时间
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public long time;
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

    /// <summary>
    /// 添加套牌 协议:8
    /// </summary>
	[Proto(value=8,description="添加套牌")]
	[ProtoContract]
	public class CardGroupRequest
	{
        /// <summary>
        ///  套牌id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string id;
        /// <summary>
        ///  name
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string name;
        /// <summary>
        ///  基地id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string baseInitId;
        /// <summary>
        ///  卡牌ids
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public List<string> cardIds;

	}

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

    /// <summary>
    /// 匹配 协议:-20
    /// </summary>
	[Proto(value=-20,description="匹配")]
	[ProtoContract]
	public class MatchResponse
	{
        /// <summary>
        ///  房间
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public RoomInfo room;
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

	[ProtoContract]
	public class RoomInfo
	{
        /// <summary>
        ///  房间id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string id;
        /// <summary>
        ///  场景icon
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string background;
        /// <summary>
        ///  我方信息
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public BattleInfo attInfo;
        /// <summary>
        ///  敌方信息
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public BattleInfo defInfo;
        /// <summary>
        ///  随机的索引
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public int index;

	}

	[ProtoContract]
	public class BattleInfo
	{
        /// <summary>
        ///  玩家id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  玩家昵称
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string name;
        /// <summary>
        ///  玩家等级
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public int lv;
        /// <summary>
        ///  玩家头像
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string icon;
        /// <summary>
        ///  基地信息
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public BaseInfo baseInfo;
        /// <summary>
        ///  当前回合数
        /// </summary>
		[ProtoMember(6, IsRequired = false)]
		public int round;
        /// <summary>
        ///  资源
        /// </summary>
		[ProtoMember(7, IsRequired = false)]
		public int resource;
        /// <summary>
        ///  手牌
        /// </summary>
		[ProtoMember(8, IsRequired = false)]
		public List<CardInfo> handCard;
        /// <summary>
        ///  桌子上的牌
        /// </summary>
		[ProtoMember(9, IsRequired = false)]
		public List<CardInfo> tableCard;
        /// <summary>
        ///  牌堆里的牌
        /// </summary>
		[ProtoMember(10, IsRequired = false)]
		public List<CardInfo> heapCard;
        /// <summary>
        ///  已使用的牌
        /// </summary>
		[ProtoMember(11, IsRequired = false)]
		public List<CardInfo> outCard;

	}

	[ProtoContract]
	public class BaseInfo
	{
        /// <summary>
        ///  基地id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string id;
        /// <summary>
        ///  名称
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string name;
        /// <summary>
        ///  血量
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public int blood;
        /// <summary>
        ///  消耗
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public int cost;
        /// <summary>
        ///  科技等级
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public int science;
        /// <summary>
        ///  技能
        /// </summary>
		[ProtoMember(6, IsRequired = false)]
		public string skill;
        /// <summary>
        ///  美术资源
        /// </summary>
		[ProtoMember(7, IsRequired = false)]
		public string art;
        /// <summary>
        ///  描述
        /// </summary>
		[ProtoMember(8, IsRequired = false)]
		public string desc;

	}

	[ProtoContract]
	public class CardInfo
	{
        /// <summary>
        ///  唯一id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string uniqueId;
        /// <summary>
        ///  id
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string id;
        /// <summary>
        ///  名称
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string name;
        /// <summary>
        ///  血量
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public int blood;
        /// <summary>
        ///  攻击
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public int att;
        /// <summary>
        ///  种族
        /// </summary>
		[ProtoMember(6, IsRequired = false)]
		public int race;
        /// <summary>
        ///  类型
        /// </summary>
		[ProtoMember(7, IsRequired = false)]
		public int type;
        /// <summary>
        ///  对应技能
        /// </summary>
		[ProtoMember(8, IsRequired = false)]
		public List<string> skill;
        /// <summary>
        ///  需要科技
        /// </summary>
		[ProtoMember(9, IsRequired = false)]
		public int science;
        /// <summary>
        ///  消耗
        /// </summary>
		[ProtoMember(10, IsRequired = false)]
		public int cost;
        /// <summary>
        ///  状态
        /// </summary>
		[ProtoMember(11, IsRequired = false)]
		public int status;
        /// <summary>
        ///  行动力
        /// </summary>
		[ProtoMember(12, IsRequired = false)]
		public int action;
        /// <summary>
        ///  buff
        /// </summary>
		[ProtoMember(13, IsRequired = false)]
		public List<string> buff;
        /// <summary>
        ///  美术资源
        /// </summary>
		[ProtoMember(14, IsRequired = false)]
		public string art;

	}

    /// <summary>
    /// 出牌 协议:21
    /// </summary>
	[Proto(value=21,description="出牌")]
	[ProtoContract]
	public class EjectionRequest
	{
        /// <summary>
        ///  房间id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string roomId;
        /// <summary>
        ///  出牌id
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string uniqueId;
        /// <summary>
        ///  摆放位置
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public int position;
        /// <summary>
        ///  目标
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string targetId;

	}

    /// <summary>
    /// 出牌 协议:-21
    /// </summary>
	[Proto(value=-21,description="出牌")]
	[ProtoContract]
	public class EjectionResponse
	{
        /// <summary>
        ///  用户id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  卡牌唯一id
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string uniqueId;
        /// <summary>
        ///  卡牌id
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public string cardId;
        /// <summary>
        ///  摆放位置
        /// </summary>
		[ProtoMember(6, IsRequired = false)]
		public int position;
        /// <summary>
        ///  目标
        /// </summary>
		[ProtoMember(7, IsRequired = false)]
		public int target;
        /// <summary>
        ///  技能
        /// </summary>
		[ProtoMember(8, IsRequired = false)]
		public List<SkillInfo> skill;
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

	[ProtoContract]
	public class SkillInfo
	{
        /// <summary>
        ///  技能id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string uniqueId;
        /// <summary>
        ///  技能id
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string id;
        /// <summary>
        ///  受影响的卡牌id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public List<string> target;

	}

    /// <summary>
    /// base lv 协议:22
    /// </summary>
	[Proto(value=22,description="base lv")]
	[ProtoContract]
	public class BaseLvRequest
	{
        /// <summary>
        ///  roomId
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string roomId;

	}

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

    /// <summary>
    /// 卡牌行动 协议:23
    /// </summary>
	[Proto(value=23,description="卡牌行动")]
	[ProtoContract]
	public class ActionRequest
	{
        /// <summary>
        ///  房间id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string roomId;
        /// <summary>
        ///  出牌id
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string uniqueId;
        /// <summary>
        ///  目标
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string targetId;

	}

    /// <summary>
    /// 卡牌行动 协议:-23
    /// </summary>
	[Proto(value=-23,description="卡牌行动")]
	[ProtoContract]
	public class ActionResponse
	{
        /// <summary>
        ///  用户id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  卡牌id
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string uniqueId;
        /// <summary>
        ///  目标
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public string targetId;
        /// <summary>
        ///  技能
        /// </summary>
		[ProtoMember(6, IsRequired = false)]
		public List<SkillInfo> skill;
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

    /// <summary>
    /// base action 协议:24
    /// </summary>
	[Proto(value=24,description="base action")]
	[ProtoContract]
	public class BaseActionRequest
	{
        /// <summary>
        ///  roomId
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string roomId;
        /// <summary>
        ///  target
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string targetId;

	}

    /// <summary>
    /// 基地行动 协议:-24
    /// </summary>
	[Proto(value=-24,description="基地行动")]
	[ProtoContract]
	public class BaseActionResponse
	{
        /// <summary>
        ///  用户id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  技能
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public List<SkillInfo> skill;
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

    /// <summary>
    /// roundOver 协议:-25
    /// </summary>
	[Proto(value=-25,description="roundOver")]
	[ProtoContract]
	public class RoundResponse
	{
        /// <summary>
        ///  房间id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string roomId;
        /// <summary>
        ///  回合信息
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public RoundInfo nri;
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

	[ProtoContract]
	public class RoundInfo
	{
        /// <summary>
        ///  攻击方id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string attId;
        /// <summary>
        ///  回合数
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public int round;
        /// <summary>
        ///  资源数
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public int resouce;
        /// <summary>
        ///  新抓到的卡牌
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public List<CardInfo> add;
        /// <summary>
        ///  需要加行动力的卡牌id,攻击方
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public List<string> action;
        /// <summary>
        ///  需要移除buff的卡牌id,防守方
        /// </summary>
		[ProtoMember(6, IsRequired = false)]
		public List<string> remove;

	}

    /// <summary>
    /// 回合开始选择随机手牌 协议:26
    /// </summary>
	[Proto(value=26,description="回合开始选择随机手牌")]
	[ProtoContract]
	public class RandomAgainRequest
	{
        /// <summary>
        ///  房间id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string roomId;
        /// <summary>
        ///  要随机掉的牌
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public List<int> indexs;

	}

    /// <summary>
    /// 回合开始选择随机手牌 协议:-26
    /// </summary>
	[Proto(value=-26,description="回合开始选择随机手牌")]
	[ProtoContract]
	public class RandomAgainResponse
	{
        /// <summary>
        ///  roomId
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string roomId;
        /// <summary>
        ///  pid
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  手牌信息
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public List<CardInfo> hand;
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

    /// <summary>
    /// 套牌推送 协议:-30
    /// </summary>
	[Proto(value=-30,description="套牌推送")]
	[ProtoContract]
	public class CardGroupNotify
	{
        /// <summary>
        ///  玩家id
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  套牌组
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public List<CardGroupInfo> cardGroup;
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

	[ProtoContract]
	public class CardGroupInfo
	{
        /// <summary>
        ///  id
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public string id;
        /// <summary>
        ///  属于哪个玩家
        /// </summary>
		[ProtoMember(2, IsRequired = false)]
		public string pid;
        /// <summary>
        ///  卡组名称
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public string name;
        /// <summary>
        ///  基地
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string baseInitId;
        /// <summary>
        ///  卡牌组
        /// </summary>
		[ProtoMember(5, IsRequired = false)]
		public List<string> cardIds;

	}

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

    /// <summary>
    /// 飘字 协议:-801
    /// </summary>
	[Proto(value=-801,description="飘字")]
	[ProtoContract]
	public class MsgNotify
	{
        /// <summary>
        ///  消息类型
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public EMsg type;
        /// <summary>
        ///  消息
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string msg;
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

    /// <summary>
    /// 飘字 协议:-801
    /// </summary>
	[Proto(value=-801,description="飘字")]
	[ProtoContract]
	public class MsgNotify
	{
        /// <summary>
        ///  消息类型
        /// </summary>
		[ProtoMember(3, IsRequired = false)]
		public EMsg type;
        /// <summary>
        ///  消息
        /// </summary>
		[ProtoMember(4, IsRequired = false)]
		public string msg;
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

	[ProtoContract]
	public enum EMsg
	{
        /// <summary>
        ///  弹框
        /// </summary>
        /// <summary>
        ///  飘字
        /// </summary>
        /// <summary>
        ///  跑马灯
        /// </summary>

	}

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

    /// <summary>
    /// 这是一条服务器的转发 协议:-9999
    /// </summary>
	[Proto(value=-9999,description="这是一条服务器的转发")]
	[ProtoContract]
	public class OnlineResponse
	{
        /// <summary>
        ///  online pids
        /// </summary>
		[ProtoMember(1, IsRequired = false)]
		public List<string> pids;

	}

