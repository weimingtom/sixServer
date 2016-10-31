/**
 * 协议的定义(值类型分别是协议号、服务器类型、处理函数)
 **/
p=[
	6:["connector","org.ngame.server.connector.ConnectorHandler.auth"],//连接器上验证
	7:["connector","org.ngame.server.connector.ConnectorHandler.ping"],//ping协议（保活）
	8:["home","org.ngame.server.home.PlayerHandler.addCardGroup"],//添加套牌
	
  20:["home","org.ngame.server.home.BattleHandler.match"], //匹配
  21:["home","org.ngame.server.home.BattleHandler.ejection"], //出牌
  22:["home","org.ngame.server.home.BattleHandler.baseLv"], //基地升级
  23:["home","org.ngame.server.home.BattleHandler.action"], //卡牌行动
  24:["home","org.ngame.server.home.BattleHandler.baseAction"], //基地攻击
  25:["home","org.ngame.server.home.BattleHandler.roundOver"], //结束回合randomAgain
  26:["home","org.ngame.server.home.BattleHandler.randomAgain"], //选择随机手牌
  27:["home","org.ngame.server.home.BattleHandler.surrender"], //我选择死亡
  //-801 飘字通知
  900:["home","org.ngame.server.home.TestHandler.testEM"], //测试
  99:["home","org.ngame.server.home.TestHandler.version"]//苏杭测试
  
  
];