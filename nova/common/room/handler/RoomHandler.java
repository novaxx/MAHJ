package nova.common.room.handler;

import java.util.HashMap;
import nova.common.room.data.PlayerInfo;

public interface RoomHandler {
	public void onRoomInfoChange(int roomId, HashMap<Integer, PlayerInfo> players);
}
