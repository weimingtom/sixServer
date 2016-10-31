
ˆ
protocol.protoprotocol"U
AuthRequest
	ucenterId (
userCode (	
testing (
serverid (":
AuthResponse
pid (	
success (
info (	"
Ping
time ("3
Pong
time (
success (
info (	"Q
CardGroupRequest

id (	
name (	

baseInitId (	
cardIds (	"#
MatchRequest
cardGroupId (	"P
MatchResponse 
room (2.protocol.RoomInfo
success (
info (	"x
RoomInfo

id (	

background (	%
attInfo (2.protocol.BattleInfo%
defInfo (2.protocol.BattleInfo" 

BattleInfo
pid (	
name (	

lv (
icon (	$
baseInfo (2.protocol.BaseInfo
round (
resource ($
handCard (2.protocol.CardInfo%
	tableCard	 (2.protocol.CardInfo$
heapCard
 (2.protocol.CardInfo#
outCard (2.protocol.CardInfo"Å
CardInfo

id (	
name (	
blood (
att (
race (
type (
skill (	
science (
cost	 (
status
 (
action (
buff (	
art (	"|
BaseInfo

id (	
name (	
blood (
cost (
science (
skill (	
art (	
desc (	"q
EjectionRequest
roomId (	
launch (
cardId (	
position (
side (
target ("”
EjectionResponse
pid (	
cardId (	
position (
target ("
skill (2.protocol.SkillInfo
success (
info (	"'
	SkillInfo

id (	
target (	"
BaseLvRequest
roomId (	"^
BaseLvResponse
pid (	
resource (
baseLv (
success (
info (	"]
ActionRequest
roomId (	
launch (
cardId (	
side (
target ("€
ActionResponse
pid (	
cardId (	
target ("
skill (2.protocol.SkillInfo
success (
info (	"A
BaseActionRequest
roomId (	
side (
target ("d
BaseActionResponse
pid (	"
skill (2.protocol.SkillInfo
success (
info (	"
RoundRequest
roomId (	"`
RoundResponse
roomId (	 
nri (2.protocol.RoundInfo
success (
info (	"{
	RoundInfo
attId (	
round (
resouce (
add (2.protocol.CardInfo
action (	
remove (	"4
RandomAgainRequest
roomId (	
indexs ("s
RandomAgainResponse
roomId (	
pid (	 
hand (2.protocol.CardInfo
success (
info (	""
SurrenderRequest
roomId (	"i
CardGroupNotify
pid (	*
	cardGroup (2.protocol.CardGroupInfo
success (
info (	"[
CardGroupInfo

id (	
pid (	
name (	

baseInitId (	
cardIds (	"O
BattleOverNotify
roomId (	
flag (
success (
info (	"U
	MsgNotify
type (2.protocol.EMsg
msg (	
success (
info (	"
OnlineRequest
pid (	"
OnlineResponse
pids (	*&
EMsg
Form
Fly
Marquee