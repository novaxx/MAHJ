package nova.common.room;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nova.common.room.data.PlayerInfo;
import nova.common.room.data.RoomInfo;

public class RoomController {
	public final static int NO_ROOM = -1;
	public final static int FULL = -2;
	public final static int RUNNING = -3;
	private static final Object mLock = new Object();
	private static HashMap<String, RoomController> mInstances = new HashMap<String, RoomController>();
	private HashMap<Integer, RoomManager> mRoomManagers = new HashMap<Integer, RoomManager>();
	private String mRoomtype;

	private RoomController(String type) {
		mRoomtype = type;
	}

	public static RoomController getInstance(String type) {
		synchronized (mLock) {
			if (mInstances.get(type) == null) {
				mInstances.put(type, new RoomController(type));
			}
			return mInstances.get(type);
		}
	}

	public RoomManager getRoomManager(int roomId) {
		if (mRoomManagers.get(roomId) == null) {
			mRoomManagers.put(roomId, new RoomManager(roomId, mRoomtype));
		}
		return mRoomManagers.get(roomId);
	}

	public int joinRoom(PlayerInfo player) {
		int roomId = searchLiveRoom(player.getId());
		if (roomId < 0) {
			roomId = searchFreeRoom();
		}
		mRoomManagers.get(roomId).getRoomInfo().addPlayer(player);
		return roomId;
	}

	public int joinRoom(int roomId, PlayerInfo player) {
		if (mRoomManagers.get(roomId) == null) {
			return NO_ROOM;
		}

		RoomInfo room = mRoomManagers.get(roomId).getRoomInfo();
		if (room.isRunning()) {
			return RUNNING;
		}

		if (room.isPlayerFilled()) {
			return FULL;
		}

		room.addPlayer(player);
		return roomId;
	}

	public int createRoom(PlayerInfo player) {
		int roomId = searchEmptyRoom();
		mRoomManagers.get(roomId).getRoomInfo().addPlayer(player);
		return roomId;
	}

	public int leaveRoom(int roomId, PlayerInfo player) {
		if (mRoomManagers.get(roomId) == null) {
			return -1;
		}

		mRoomManagers.get(roomId).getRoomInfo().removePlayer(player);
		return 0;
	}

	public int cleanRoom(int roomId) {
		if (mRoomManagers.get(roomId) == null) {
			return -1;
		}

		mRoomManagers.remove(roomId);
		return 0;
	}

	private int searchLiveRoom(int playerId) {
		Set set = mRoomManagers.entrySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (((RoomManager) entry.getValue()).getRoomInfo().containPlayer(playerId)) {
				return (Integer) (entry.getKey());
			}
		}
		return -1;
	}

	private int searchFreeRoom() {
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			RoomManager roomManager = getRoomManager(i);
			RoomInfo room = roomManager.getRoomInfo();
			if (room.isPlayerFilled() || room.isRunning()) {
				continue;
			}

			return i;
		}

		return -1;
	}

	private int searchEmptyRoom() {
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			if (mRoomManagers.get(i) == null) {
				mRoomManagers.put(i, new RoomManager(i, mRoomtype));
				return i;
			}
		}

		return -1;
	}
}
