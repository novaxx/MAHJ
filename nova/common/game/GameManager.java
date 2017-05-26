package nova.common.game;

import nova.common.game.mahjong.MahjGameManager;

public class GameManager {
	
	public static final int GAME_MAHJ = 0;
	
	public static GameManager createManager(int roomId, String gameType) {
		if (gameType.equals("mahj")) {
			return new MahjGameManager(roomId);
		} else {
			return new GameManager(roomId);
		}
	}
	
	protected GameManager(int roomId) {
		
	}
	
	public void startGame() {
		
	}
	
	public void stopGame() {
		
	}
}