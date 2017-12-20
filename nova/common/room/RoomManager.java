package nova.common.room;

import nova.common.GameHandler;
import nova.common.GameManager;
import nova.common.room.data.RoomInfo;
import nova.common.room.handler.RoomHandler;

public class RoomManager {

	private int mRoomId;
	private RoomInfo mRoomInfo;
	private RoomHandler mRoomHandler;
	private GameManager mGameManager;
	private int mGameStartDelay = 5;
	private boolean mIsRunning = false;

	private Runnable mGameRunnable = new Runnable() {
		public void run() {
			mIsRunning = true;
			int count = 0;
			while (count <= mGameStartDelay) {
				count++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (mRoomInfo.isPlayerFilled()) {
					break;
				}

				if (mRoomHandler != null) {
					mRoomHandler.onRoomInfoChange(mRoomId, mRoomInfo.getPlayers());
				}
			}

			if (!mRoomInfo.isPlayerFilled()) {
				mRoomInfo.fillAutoPlayer();
			}
			if (mRoomHandler != null) {
				mRoomHandler.onRoomInfoChange(mRoomId, mRoomInfo.getPlayers());
			}
			mGameManager.startGame();
			mRoomInfo.setRunning(true);
		}
	};

	public RoomManager(int roomId, GameHandler handler, int gameType) {
		mRoomId = roomId;
		mRoomInfo = new RoomInfo();
		mGameManager = GameManager.createManager(roomId, handler, gameType);
	}

	public void setTestGameDelay(int delay) {
		mGameStartDelay = delay;
	}

	public void setRoomInfo(RoomInfo info) {
		mRoomInfo = info;
	}

	public RoomInfo getRoomInfo() {
		return mRoomInfo;
	}

	public GameManager getGameManager() {
		return mGameManager;
	}

	public void setRoomHandler(RoomHandler handler) {
		mRoomHandler = handler;
	}

	public void startGame() {
		if (!mIsRunning) {
			new Thread(mGameRunnable).start();
		}
	}

	public void stopGame() {
		mIsRunning = false;
		mRoomInfo.setRunning(false);
		mRoomInfo.removeAllPlayer();
		mGameManager.stopGame();
	}
	
	public void resumeGame() {
		mGameManager.resumeGame();
	}

	public boolean isPlayerFilled() {
		return mRoomInfo.isPlayerFilled();
	}
}
