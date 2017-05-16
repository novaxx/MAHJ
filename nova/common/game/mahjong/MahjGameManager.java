package nova.common.game.mahjong;

import nova.common.game.mahjong.MahjGameStage.StageCallBack;
import nova.common.game.mahjong.data.MahjGameData;
import nova.common.game.mahjong.handler.GameLogger;
import nova.common.game.mahjong.handler.MahjGameHandler;


public class MahjGameManager implements StageCallBack {

	private int mRoomId;
	private MahjGameStage mStage;
	private MahjManager mMahjManager;
	private MahjGameHandler mHandler;
	private GameLogger mLogger;
	private MahjGameData mGameData;
	
	@Override
	public void onStageEnd(int stage) {
		switch (stage) {
		case MahjGameStage.GET_MAHJ_GOD:
			mGameData.setGod(mMahjManager.getGodData());
			break;
		
		case MahjGameStage.GET_MAHJ_WAIT:
			getLatestData();
			break;
			
		case MahjGameStage.OUT_MAHJ_WAIT:
			autoOutData();
			break;
			
		case MahjGameStage.MATCH_MAHJ_WAIT:
			autoMatchData();
			break;
			
		default:
			break;
		}
		updateGameInfoForHandler();
	}
	
	@Override
	public boolean hasMatchType() {
		return mMahjManager.hasMatchType();
	}
	
	@Override
	public void clearMatchType() {
		mMahjManager.clearMatchType();
	}
	
	public void setLogger(GameLogger logger) {
		mLogger = logger;
	}
	
	public void setHandler(MahjGameHandler handler) {
		mHandler = handler;
	}
	
	public MahjGameManager(int roomId) {
		mRoomId = roomId;
		mMahjManager = new MahjManager();
		mGameData = new MahjGameData();
		mStage = new MahjGameStage();
		mStage.setStageHandler(this);
	}
	
	public void startGame() {
		initGameData();
		mStage.start();
	}
	
	public void stopGame() {
		mLogger.d("zhangxx", "stopGame");
		mStage.stop();
	}
	
	private void initGameData() {
		mMahjManager.initDatas();
		mGameData.initDatas();
		mGameData.setDatas(mMahjManager.getMahjDatas());
		updateGameInfoForHandler();
	}
	
	private void updateGameInfoForHandler() {
		if (mHandler != null) {
			mHandler.onGameInfoChange(mRoomId, mGameData, mMahjManager.getPlayerDatas());
		}
	}
	
	private void getLatestData() {
		mGameData.setCurrent(mGameData.getCurrent() + 1);
		mMahjManager.getLatestData(mGameData.getCurrent());
	}
	
	private void autoOutData() {
		mMahjManager.autoOutData(mGameData.getCurrent());
		
		if (mMahjManager.getMahjDatas().size() <= 0) {
			stopGame();
		}
	}
	
	private void autoMatchData() {
		int playerId = mMahjManager.getFirstMatchPlayer(mGameData.getCurrent());
		int matchType = mMahjManager.getFirstMatchType(playerId);
		mMahjManager.obtainMatchData(playerId, mGameData.getCurrent(), matchType);
		mGameData.setCurrent(playerId);
		
	}
}
