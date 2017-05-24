package nova.common.game.mahjong.handler;

import nova.common.game.mahjong.data.MahjResponeData;

public interface MahjGameHandler {
	public void onGameInfoChange(int roomId, MahjResponeData data);
}
