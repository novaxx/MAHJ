package nova.common.game.mahjong;

import java.util.ArrayList;

import nova.common.GameCommand;
import nova.common.GameManager;
import nova.common.game.mahjong.MahjGameStage.StageCallBack;
import nova.common.game.mahjong.data.MahjData;
import nova.common.game.mahjong.data.MahjGameData;
import nova.common.game.mahjong.data.MahjResponeData;
import nova.common.game.mahjong.handler.GameLogger;
import nova.common.game.mahjong.handler.MahjGameDispatcher;
import nova.common.game.mahjong.handler.MahjGameHandler;
import nova.common.game.mahjong.util.MahjConstant;
import nova.common.room.RoomController;
import nova.common.room.data.PlayerInfo;

public class MahjGameManager extends GameManager implements StageCallBack, MahjGameDispatcher {

	private int mRoomId;
	private MahjGameStage mStage;
	private MahjManager mMahjManager;
	private MahjGameHandler mHandler;
	private GameLogger mLogger = GameLogger.getInstance();
	private MahjGameData mGameData;

	@Override
	public void onStageEnd(int stage) {
		switch (stage) {
		case MahjGameStage.GET_MAHJ_GOD:
			mMahjManager.updateGodData();
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
			
		case MahjGameStage.MAHJ_END:
			pauseGame();
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
	public int getOperateType() {
		return mMahjManager.getPlayerDatas().get(mGameData.getCurrent()).getOperateType();
	}

	@Override
	public void onDataOutEnd(boolean isMatched) {
		if (!isMatched) {
			mGameData.setCurrent(mGameData.getCurrent() + 1);
		}

		mMahjManager.clearMatchType();
	}
	
	@Override
	public void onTimeChange() {
		mMahjManager.clearOperateType();
	}
	
	@Override
	public boolean isAutoOperator() {
		PlayerInfo player = RoomController.getInstance(GameCommand.MAHJ_TYPE_GAME).getRoomManager(mRoomId)
				.getRoomInfo().getPlayer(mGameData.getCurrent());
		if (player == null) {
			return true;
		}
		
		int type = player.getType();
		return type == 1;
	}

	public void setLogger(GameLogger logger) {
		mLogger = logger;
	}

	public void setHandler(MahjGameHandler handler) {
		mHandler = handler;
	}

	public MahjGameManager(int roomId) {
		super(roomId);
		mRoomId = roomId;
		mMahjManager = new MahjManager();
		mGameData = new MahjGameData();
		mStage = new MahjGameStage();
		mStage.setStageHandler(this);
	}

	@Override
	public void startGame() {
		super.startGame();
		initGameData();
		mStage.start();
	}

	@Override
	public void stopGame() {
		super.stopGame();
		mLogger.d("zhangxx", "stopGame");
		mStage.stop();
	}
	
	public void pauseGame() {
		mLogger.d("zhangxx", "pauseGame");
		startGame();
	}

	@Override
	public void activeOutData(int playerId, int dataIndex) {
		mLogger.d("zhangxx", "activeOutData, current : " + mGameData.getCurrent() + ", playerId : " + playerId + ", dataIndex : " + dataIndex);
		if (mGameData.getCurrent() != playerId) {
			return;
		}
		
		if (mMahjManager.containData(playerId, dataIndex)) {
			updateOutData(playerId, new MahjData(dataIndex));
		} else {
			autoOutData();
		}
		mStage.updateStage();
		updateGameInfoForHandler();
	}

	@Override
	public void activeOperateData(int playerId, int operateType) {
		mLogger.d("zhangxx", "activeOperateData, current : " + mGameData.getCurrent() + ", playerId : " + playerId + ", operateType : " + operateType);
		if (operateType != MahjConstant.MAHJ_MATCH_PENG && operateType != MahjConstant.MAHJ_MATCH_GANG 
				&& operateType != MahjConstant.MAHJ_MATCH_CHI && operateType != MahjConstant.MAHJ_MATCH_TING
				&& operateType != MahjConstant.MAHJ_MATCH_HU) {
			return;
		}
		
		if (operateType == MahjConstant.MAHJ_MATCH_TING) {
			mMahjManager.getPlayerDatas().get(playerId).setOperateType(MahjConstant.MAHJ_MATCH_TING);
			return;
		}
		
		if (operateType == MahjConstant.MAHJ_MATCH_HU) {
			mMahjManager.getPlayerDatas().get(playerId).setOperateType(MahjConstant.MAHJ_MATCH_HU);
			mStage.updateStage();
			updateGameInfoForHandler();
			return;
		}
		
		if (mGameData.getCurrent() == playerId) {
			if (operateType == MahjConstant.MAHJ_MATCH_GANG) {
				ArrayList<Integer> gangList = mMahjManager.getPlayerDatas().get(playerId).getGangListFromDatas();
				if (gangList != null && gangList.size() > 0) {
					mMahjManager.getPlayerDatas().get(mGameData.getCurrent()).operateGangData(gangList.get(0));
					mMahjManager.getPlayerDatas().get(mGameData.getCurrent()).setOperateType(MahjConstant.MAHJ_MATCH_GANG);
					mStage.updateStage();
					updateGameInfoForHandler();
				}
			}
			return;
		}
		
		if ((mMahjManager.getPlayerDatas().get(playerId).getMatchType() & operateType) != operateType) {
			return;
		}
		
		updateMatchData(playerId, operateType);
		
		mStage.updateStage();
		updateGameInfoForHandler();
	}

	private void initGameData() {
		mMahjManager.initDatas();
		mGameData.initDatas();
		mGameData.setDatas(mMahjManager.getMahjDatas());
		updateGameInfoForHandler();
	}

	private void updateGameInfoForHandler() {
		if (mHandler != null) {
			mHandler.onGameInfoChange(mRoomId, new MahjResponeData(mGameData, mMahjManager.getPlayerDatas()));
		}
	}

	private void getLatestData() {
		mMahjManager.getLatestData(mGameData.getCurrent());
	}

	private void autoOutData() {
		// 胡牌
		if (mMahjManager.getPlayerDatas().get(mGameData.getCurrent()).isHuEnable()) {
			mMahjManager.getPlayerDatas().get(mGameData.getCurrent()).setOperateType(MahjConstant.MAHJ_MATCH_HU);
			return;
		}
		
		//杠
		ArrayList<Integer> gangList = mMahjManager.getPlayerDatas().get(mGameData.getCurrent()).getGangListFromDatas();
		if (gangList != null && gangList.size() > 0) {
			mMahjManager.getPlayerDatas().get(mGameData.getCurrent()).operateGangData(gangList.get(0));
			mMahjManager.getPlayerDatas().get(mGameData.getCurrent()).setOperateType(MahjConstant.MAHJ_MATCH_GANG);
			return;
		}
		
		MahjData outData = mMahjManager.getAutoOutData(mGameData.getCurrent());
		updateOutData(mGameData.getCurrent(), outData);
	}

	private void updateOutData(int playerId, MahjData outData) {
		mMahjManager.updateOutData(playerId, outData);
		// 更新出牌的玩家
		mGameData.setLastout(playerId);

		if (mMahjManager.getMahjDatas().size() <= 0) {
			pauseGame();
		}
	}

	private void autoMatchData() {
		int playerId = mMahjManager.getFirstMatchPlayer(mGameData.getCurrent());
		int matchType = mMahjManager.getFirstMatchType(playerId);
		updateMatchData(playerId, matchType);
	}

	private void updateMatchData(int playerId, int matchType) {
		mMahjManager.obtainMatchData(playerId, mGameData.getCurrent(), matchType);
		mGameData.setCurrent(playerId);
		// 清空出牌的玩家
		mGameData.clearLastout();
	}
}
