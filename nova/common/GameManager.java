package nova.common;

import nova.common.game.mahjong.MahjGameManager;

public class GameManager {
	
	public static final int GAME_MAHJ = 0;
	
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
	
	public void startGame() {
		
	}
	
	public void resumeGame() {
		
	}
	
	public void stopGame() {
		
	}
	
	public void pauseGame() {
		
	}
}