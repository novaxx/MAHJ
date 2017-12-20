package nova.common;

import nova.common.game.mahjong.MahjGameManager;

public class GameManager {
	
	public static final int GAME_MAHJ = 0;
	public static final int GAME_STATE_INIT = 0;
	public static final int GAME_STATE_PAUSE = 1;
	public static final int GAME_STATE_START = 2;
	public static final int GAME_STATE_RESUME = 3;
	private int mGameState;
	protected int mRoomId;
	private GameHandler mHandler;
	
	public static GameManager createManager(int roomId, GameHandler handler, int gameType) {
		GameManager manager;
		switch (gameType) {
		case GameCommand.MAHJ_TYPE_GAME:
			manager = new MahjGameManager(roomId);
			break;
			
		default:
			manager = new GameManager(roomId);
			break;
		}
		
		manager.setGameHandler(handler);
		
		return manager;
	}
	
	protected GameManager(int roomId) {
		mRoomId = roomId;
	}
	
	public void setGameHandler(GameHandler handler) {
		mHandler = handler;
	}
	
	public boolean isGameRunning() {
		return mGameState == GAME_STATE_START || mGameState == GAME_STATE_RESUME;
	}
	
	public void startGame() {
		if (mHandler != null) {
			mHandler.start();
		}
		mGameState = GAME_STATE_START;
	}
	
	public void resumeGame() {
		mGameState = GAME_STATE_RESUME;
	}
	
	public void stopGame() {
		if (isGameRunning() && mHandler != null) {
			mHandler.end(mRoomId, getWinner());
		}
		mGameState = GAME_STATE_INIT;
	}
	
	public void pauseGame() {
		if (isGameRunning() && mHandler != null) {
			mHandler.end(mRoomId, getWinner());
		}
		mGameState = GAME_STATE_PAUSE;
	}
	
	protected int getWinner() {
		return -1;
	}
}