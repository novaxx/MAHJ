package nova.common;

import nova.common.game.mahjong.MahjGameManager;

public class GameManager {
	
	public static final int GAME_MAHJ = 0;
	public static final int GAME_STATE_INIT = 0;
	public static final int GAME_STATE_PAUSE = 1;
	public static final int GAME_STATE_START = 2;
	public static final int GAME_STATE_RESUME = 3;
	private int mGameState;
	
	public static GameManager createManager(int roomId, int gameType) {
		switch (gameType) {
		case GameCommand.MAHJ_TYPE_GAME:
			return new MahjGameManager(roomId);

		default:
			return new GameManager(roomId);
		}
	}
	
	protected GameManager(int roomId) {
		
	}
	
	public boolean isGameRunning() {
		return mGameState == GAME_STATE_START || mGameState == GAME_STATE_RESUME;
	}
	
	public void startGame() {
		mGameState = GAME_STATE_START;
	}
	
	public void resumeGame() {
		mGameState = GAME_STATE_RESUME;
	}
	
	public void stopGame() {
		mGameState = GAME_STATE_INIT;
	}
	
	public void pauseGame() {
		mGameState = GAME_STATE_PAUSE;
	}
}