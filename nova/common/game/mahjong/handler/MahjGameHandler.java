package nova.common.game.mahjong.handler;

import java.util.HashMap;

import nova.common.game.mahjong.data.MahjResponeData;
import nova.common.game.mahjong.data.MahjResultData;

public interface MahjGameHandler {
	public void onGameInfoChange(int roomId, MahjResponeData data);
	public void handleGameResult(int roomId, HashMap<Integer, MahjResultData> results);
}
