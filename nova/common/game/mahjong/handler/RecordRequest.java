package nova.common.game.mahjong.handler;

public class RecordRequest {

	private int mRoomId;
	private String mBody;
	private String mTime;
	
	public RecordRequest(int roomId, String time, String body) {
		mRoomId = roomId;
		mBody = body;
		mTime = time;
	}
	
	public int getRoom() {
		return mRoomId;
	}
	
	public String getBody() {
		return mBody;
	}
	
	public String getTime() {
		return mTime;
	}
	
	public String getRoomId_Time() {
		return mRoomId + "_" + mTime;
	}
}
