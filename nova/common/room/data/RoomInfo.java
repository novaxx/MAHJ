package nova.common.room.data;

import java.util.HashMap;

import nova.common.game.mahjong.handler.GameLogger;

public class RoomInfo {
	private static final String TAG = "RoomInfo";
	private boolean mIsRunning;
	private HashMap<Integer, PlayerInfo> mPlayers = new HashMap<Integer, PlayerInfo>();

	private static final int PLAYER_MAX = 4;

	public RoomInfo() {

	}

	public void setRunning(boolean isRun) {
		mIsRunning = isRun;
	}

	public boolean isRunning() {
		return mIsRunning;
	}

	public void addPlayer(PlayerInfo player) {
		if (isPlayerFilled()) {
			return;
		}

		for (int i = 0; i < PLAYER_MAX; i++) {
			if (mPlayers.get(i) == null) {
				mPlayers.put(i, player);
				break;
			}
		}
	}

	public void removePlayer(PlayerInfo player) {
		for (int i = 0; i < PLAYER_MAX; i++) {
			if (mPlayers.get(i).getId() == player.getId()) {
				mPlayers.remove(i);
				break;
			}
		}
	}

	public void removeAllPlayer() {
		mPlayers.clear();
	}

	public void replacePlayer(int sp, int tp) {
		if (!mPlayers.containsKey(tp)) {
			mPlayers.put(tp, mPlayers.get(sp));
			mPlayers.remove(sp);
		} else {
			PlayerInfo tmpPlayer = mPlayers.get(tp);
			mPlayers.remove(tp);
			mPlayers.put(tp, mPlayers.get(sp));
			mPlayers.remove(sp);
			mPlayers.put(sp, tmpPlayer);
		}
	}

	public HashMap<Integer, PlayerInfo> getPlayers() {
		return mPlayers;
	}

	public PlayerInfo getPlayer(int id) {
		if (id < 0 || id >= 4) {
			GameLogger.getInstance().e(TAG, "getPlayer error : index " + id + " is anomaly value !!!");
			return null;
		}
		return mPlayers.get(id);
	}
	
	public int getIndexForPlayerId(int playerId) {
		for (int i = 0; i < PLAYER_MAX; i++) {
			if (mPlayers.get(i) == null) {
				continue;
			}

			if (mPlayers.get(i).getId() == playerId) {
				return i;
			}
		}
		
		return -1;
	}

	public boolean containPlayer(int playerId) {
		for (int i = 0; i < PLAYER_MAX; i++) {
			if (mPlayers.get(i) == null) {
				continue;
			}

			if (mPlayers.get(i).getId() == playerId) {
				return true;
			}
		}

		return false;
	}

	public boolean isPlayerFilled() {
		return mPlayers.size() >= PLAYER_MAX;
	}
	
	public void updateTypeForPlayer(int playerId, boolean isAuto) {
		for (int i = 0; i < PLAYER_MAX; i++) {
			if (mPlayers.get(i) == null) {
				continue;
			}

			if (mPlayers.get(i).getId() == playerId) {
				mPlayers.get(i).setType(isAuto ? 1 : 0);
			}
		}
	}

	public boolean isNormalPlayer(int id) {
		return mPlayers.get(id).isNormalPlayer();
	}

	public void fillAutoPlayer() {
		for (int i = 0; i < PLAYER_MAX; i++) {
			if (mPlayers.get(i) == null) {
				PlayerInfo player = new PlayerInfo();
				player.setName("倔强的电脑" + i);
				player.setType(1);
				mPlayers.put(i, player);
			}
		}
	}
}
