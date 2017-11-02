package nova.common.room;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nova.common.game.mahjong.handler.GameLogger;
import nova.common.room.data.PlayerInfo;
import nova.common.room.data.RoomInfo;

public class RoomController {
	private static final String TAG = "RoomController";
	public final static int NO_ROOM = -1;
	public final static int FULL = -2;
	public final static int RUNNING = -3;
	private static final Object mLock = new Object();
	private static HashMap<Integer, RoomController> mInstances = new HashMap<Integer, RoomController>();
	private HashMap<Integer, RoomManager> mRoomManagers = new HashMap<Integer, RoomManager>();
	private int mRoomtype;

	private RoomController(int type) {
		mRoomtype = type;
	}

	public static RoomController getInstance(int type) {
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
	
	@SuppressWarnings("rawtypes")
	public void updateRoomManagerForPlayerOffline(int playerId) {
		Set set = mRoomManagers.entrySet();
		Iterator it=set.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			RoomManager roomManager = (RoomManager)entry.getValue();
			if (roomManager.getRoomInfo().containPlayer(playerId)) {
				int roomId = (Integer)(entry.getKey());
				if (roomManager.getRoomInfo().isRunning()) {
					roomManager.getRoomInfo().updateTypeForPlayer(playerId, true);
					GameLogger.getInstance().i(TAG, "updateRoomManagerForPlayerOffline set palyer auto success, playerId : " + roomId + ", playerId : " + playerId);
				} else {
					int index = roomManager.getRoomInfo().getIndexForPlayerId(playerId);
					if (index >= 0) {
						leaveRoom(roomId, roomManager.getRoomInfo().getPlayer(index));
						GameLogger.getInstance().i(TAG, "updateRoomManagerForPlayerOffline leave room success, room : " + roomId + ", playerId : " + playerId);
					} else {
						GameLogger.getInstance().e(TAG, "updateRoomManagerForPlayerOffline error : index " + index + " is anomaly value !!!");
					}
				}
				return;
			}
		}
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
		if (mRoomManagers.get(roomId) == null || player == null) {
			return -1;
		}

		mRoomManagers.get(roomId).getRoomInfo().removePlayer(player);
		
		if (mRoomManagers.get(roomId).getRoomInfo().getPlayers().size() <= 0) {
			cleanRoom(roomId);
		}
		
		return 0;
	}

	public int cleanRoom(int roomId) {
		if (mRoomManagers.get(roomId) == null) {
			return -1;
		}

		mRoomManagers.remove(roomId);
		return 0;
	}

	@SuppressWarnings("rawtypes")
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
