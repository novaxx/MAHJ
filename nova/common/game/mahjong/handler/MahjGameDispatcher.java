package nova.common.game.mahjong.handler;

public interface MahjGameDispatcher {
	public void activeOutData(int playerId, int dataIndex);
	public void activeMatchData(int playerId, int matchType);
}
