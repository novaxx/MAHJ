package nova.common.game.mahjong.handler;

import java.util.HashMap;
import nova.common.game.mahjong.data.MahjGameData;
import nova.common.game.mahjong.data.MahjGroupData;

public interface MahjGameHandler {
	public void onGameInfoChange(int roomId,
			MahjGameData data,
			HashMap<Integer, MahjGroupData> playerDatas);
}
