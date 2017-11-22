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
}
