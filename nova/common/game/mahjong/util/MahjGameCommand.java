package nova.common.game.mahjong.util;

public class MahjGameCommand {

	// 快速游戏
	public static final int REQUEST_GAME_START = 1201;
	// 开始游戏
	public static final int REQUEST_GAME_ROOM_START = 1202;
	// 出牌
	public static final int REQUEST_OUT_DATA = 1203;
	// 操作
	public static final int REQUEST_OPERATE_DATA = 1204;
	// 退出游戏
	public static final int REQUEST_GAME_STOP = 1205;
	// 继续游戏
	public static final int REQUEST_GAME_RESUME = 1206;
	
	// 创建房间
	public static final int REQUEST_ROOM_CREATE = 1221;
	// 加入房间
	public static final int REQUEST_ROOM_JOIN = 1222;
	// 离开房间
	public static final int REQUEST_ROOM_LEAVE = 1223;
	
	// 游戏信息变化
	public static final int RESPONE_GAME_INFO_UPDATE = 1250;
	// 游戏结果处理
	public static final int RESPONE_GAME_RESULT = 1251;
	// 玩家信息变化
	public static final int RESPONE_PLAYER_INFO_UPDATE = 1260;
	// 房间信息变化
	public static final int RESPONE_ROOM_INFO_UPDATE = 1290;
	// 房间状态变化
	public static final int RESPONE_ROOM_STATE_UPDATE = 1291;
	
	// 发送消息
	public static final int RESPONE_SEND_MESSAGE = 9000;
	// 发送语音
	public static final int RESPONE_SEND_VOICE = 9001;
	// 发送消息
	public static final int RESPONE_SEND_MESSAGE_VOICE = 9002;
	
	public class MessageType {
		public static final int TYPE_MESSAGE = 0;
		public static final int TYPE_VOICE = 1;
		public static final int TYPE_MESSAGE_VOICE = 2;
	}
	
	public class RoomState {
		public static final int ROOM_STATE_FAIL = -1;
		public static final int ROOM_STATE_JOIN = 1;
		public static final int ROOM_STATE_GAME_START = 2;
	}
	
	public static class FanType {
		public static final int TYPE_HU = 0;
		public static final int TYPE_ZIMO = 1;
		public static final int TYPE_GANG = 2;
		public static final int TYPE_FENG = 3;
		public static final int TYPE_DIAOJIAN = 4;
		public static final int TYPE_QINGYISE = 5;
		public static final int TYPE_YITIAOLONG = 6;
		public static final int TYPE_MENQIANQIN = 7;
		public static final int TYPE_QIDUI = 8;
		public static final int TYPE_SIGAN = 9;
		public static final int TYPE_LIANQIDUI = 10;
		public static final int TYPE_DASANYUAN = 11;
		public static final int TYPE_DASIXI = 12;
		public static final int TYPE_TIANHU = 13;
		public static final int TYPE_SIFANDAFA = 14;
		public static final String[] FAN_DISPLAY_TYPE = {
				"胡",                   //0
				"自摸",              //1
				"杆",                  //2
				"风",                  //3
				"吊将",             //4
				"清一色",        //5
				"一条龙",        //6
				"门清",            //7
				"七对",            //8
				"十八学士",    //9
				"连七对",        //10
				"大三元",        //11
				"大四喜",        //12
				"天胡",             //13
				"四方大发",    //14
		};
	}
}
