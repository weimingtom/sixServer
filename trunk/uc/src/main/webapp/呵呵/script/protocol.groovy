/**
 * 协议的定义(值类型分别是服务器类型、处理函数、请求协议体)
 **/
p=[
	6:["connector","org.ngame.server.connector.ConnectorHandler.auth"],//连接器上验证
	7:["connector","org.ngame.server.connector.ConnectorHandler.ping"],//ping协议（保活）
	8:["connector","org.ngame.server.connector.ConnectorHandler.createPlayer"],//创建角色
	9:["home","org.ngame.server.home.PlayerHandler.getRes"], //资源详情
  10:["home","org.ngame.server.home.PlayerHandler.randomNickyName"], //随机名字
  11:["home","org.ngame.server.home.ScienceHandler.scienceInfo"], //科技信息
  12:["home","org.ngame.server.home.ScienceHandler.uplevel"], //科技升级
];