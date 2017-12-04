package nova.common.game.mahjong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nova.common.GameCommand;
import nova.common.GameManager;
import nova.common.game.mahjong.MahjGameStage.StageCallBack;
import nova.common.game.mahjong.data.MahjData;
import nova.common.game.mahjong.data.MahjGameData;
import nova.common.game.mahjong.data.MahjResponeData;
import nova.common.game.mahjong.handler.FileLogRecorderManager;
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
	
	// 用于测试打印LOG
	public static boolean debug = false;
	private static final String DEBUG_TAG = "MJ_MSG";
	// 用于保存游戏信息到文件
	private String mStartTime;

	@Override
	public void onStageEnd(int stage) {
		switch (stage) {
		case MahjGameStage.GET_MAHJ_GOD:
			mMahjManager.updateGodData();
			mGameData.setGod(mMahjManager.getGodData());
			if (debug) {
				mLogger.i(DEBUG_TAG, "get god data : " + mGameData.getGod());
			}
			//写信息到文件
			printMessageToFile("get god data : " + mGameData.getGod());
			break;

		case MahjGameStage.GET_MAHJ_WAIT:
			getLatestData();
			if (debug) {
				mLogger.i(DEBUG_TAG, "player " + mGameData.getCurrent() + 
						" get latest data : " + mMahjManager.getPlayerDatas().get(mGameData.getCurrent()).getLatestData().getIndex());
			}
		    // 写信息到文件
			printMessageToFile("player " + mGameData.getCurrent() + 
					" get latest data : " + mMahjManager.getPlayerDatas().get(mGameData.getCurrent()).getLatestData().getIndex());
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
	
	@Override
	public boolean hasNoMahj() {
		return mMahjManager.getMahjDatas().size() <= 0;
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
		/*mMahjManager = new MahjManager();
		mGameData = new MahjGameData();
		mStage = new MahjGameStage();
		mStage.setStageHandler(this);*/
	}

	@Override
	public void startGame() {
		super.startGame();
		mLogger.d("zhangxx", "startGame");
		mStartTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		initGameData();
		mStage.start();
	}

	@Override
	public void resumeGame() {
		super.resumeGame();
		startGame();
	}
	
	@Override
	public void stopGame() {
		super.stopGame();
		mLogger.d("zhangxx", "stopGame");
		mStage.stop();
	}
	
	@Override
	public void pauseGame() {
		super.pauseGame();
		mLogger.d("zhangxx", "pauseGame");
		mStage.stop();
	}

	@Override
	public void activeOutData(int playerId, int dataIndex) {
		mLogger.d("zhangxx", "activeOutData, current : " + mGameData.getCurrent() + ", playerId : " + playerId + ", dataIndex : " + dataIndex);
		
		if (debug) {
			mLogger.i(DEBUG_TAG, "player : " + playerId + " active out data : " + dataIndex);
		}
		// 写信息到文件
		printMessageToFile("player : " + playerId + " active out data : " + dataIndex);
		
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
		
		if (debug) {
			mLogger.i(DEBUG_TAG, "player : " + playerId + " active operated " + operateType);
		}
		// 写信息到文件
		printMessageToFile("player : " + playerId + " active operated " + operateType);
		
		if (operateType != MahjConstant.MAHJ_MATCH_PENG && operateType != MahjConstant.MAHJ_MATCH_GANG 
				&& operateType != MahjConstant.MAHJ_MATCH_CHI && operateType != MahjConstant.MAHJ_MATCH_TING
				&& operateType != MahjConstant.MAHJ_MATCH_HU && operateType != MahjConstant.MAHJ_MATCH_GUO) {
			return;
		}
		
		if (operateType == MahjConstant.MAHJ_MATCH_GUO) {
			// 清空玩家match标记
			mMahjManager.getPlayerDatas().get(playerId).updateMatchType(null);
			mStage.updateStage();
			updateGameInfoForHandler();
			return;
		}
		
		if (operateType == MahjConstant.MAHJ_MATCH_TING) {
			mMahjManager.getPlayerDatas().get(playerId).setOperateType(MahjConstant.MAHJ_MATCH_TING);
			return;
		}
		
		if (operateType == MahjConstant.MAHJ_MATCH_HU) {
			mMahjManager.getPlayerDatas().get(playerId).setOperateType(MahjConstant.MAHJ_MATCH_HU);
			mGameData.setWinner(playerId);
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
		int banker = 0;
		if (mGameData != null) {
			banker = mGameData.getBanker();
		}
		mMahjManager = new MahjManager();
		mMahjManager.initDatas();
		
		mGameData = new MahjGameData();
		mGameData.setBanker(banker);
		mGameData.setDatas(mMahjManager.getMahjDatas());
		
		mStage = new MahjGameStage();
		mStage.setStageHandler(this);
		
		updateGameInfoForHandler();
	}

	private void updateGameInfoForHandler() {
		if (mHandler != null) {
			// 用于测试打印LOG
			printLogForDebug();
			// 用于测试打印LOG
			
			// 写信息到文件
			updateGameInfoToFile();
			
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
			mGameData.setWinner(mGameData.getCurrent());
			
			if (debug) {
				mLogger.i(DEBUG_TAG, "player : " + mGameData.getCurrent() + " HU !!");
			}
			// 写信息到文件
			printMessageToFile("player : " + mGameData.getCurrent() + " HU !!");
			return;
		}
		
		//杠
		ArrayList<Integer> gangList = mMahjManager.getPlayerDatas().get(mGameData.getCurrent()).getGangListFromDatas();
		if (gangList != null && gangList.size() > 0) {
			mMahjManager.getPlayerDatas().get(mGameData.getCurrent()).operateGangData(gangList.get(0));
			mMahjManager.getPlayerDatas().get(mGameData.getCurrent()).setOperateType(MahjConstant.MAHJ_MATCH_GANG);
			
			if (debug) {
				mLogger.i(DEBUG_TAG, "player : " + mGameData.getCurrent() + " GANG !!");
			}
			// 写信息到文件
			printMessageToFile("player : " + mGameData.getCurrent() + " GANG !!");
			return;
		}
		
		MahjData outData = mMahjManager.getAutoOutData(mGameData.getCurrent());
		updateOutData(mGameData.getCurrent(), outData);
		
		if (debug) {
			mLogger.i(DEBUG_TAG, "player : " + mGameData.getCurrent() + " out data : " + outData.getIndex());
		}
		// 写信息到文件
		printMessageToFile("player : " + mGameData.getCurrent() + " out data : " + outData.getIndex());
	}

	private void updateOutData(int playerId, MahjData outData) {
		mMahjManager.updateOutData(playerId, outData);
		// 更新出牌的玩家
		mGameData.setLastout(playerId);

		if (mMahjManager.getMahjDatas().size() <= 0) {
			// pauseGame();
			// 平局
			mGameData.setWinner(5);
		}
	}

	private void autoMatchData() {
		int playerId = mMahjManager.getFirstMatchPlayer(mGameData.getCurrent());
		int matchType = mMahjManager.getFirstMatchType(playerId);
		updateMatchData(playerId, matchType);
		
		if (debug) {
			mLogger.i(DEBUG_TAG, "player : " + mGameData.getCurrent() + " matched " + matchType);
		}
		// 写信息到文件
		printMessageToFile("player : " + mGameData.getCurrent() + " matched " + matchType);
	}

	private void updateMatchData(int playerId, int matchType) {
		mMahjManager.obtainMatchData(playerId, mGameData.getCurrent(), matchType);
		mGameData.setCurrent(playerId);
		// 清空出牌的玩家
		mGameData.clearLastout();
	}
	
	private void printLogForDebug() {
		if (debug) {
			mLogger.i(DEBUG_TAG, "->->->->->->->->->->->->->->->->->->->->->->");
			String remainData = "[余]";
			for (MahjData data : mGameData.getDatas()) {
				remainData = remainData + data.getIndex() + ",";
			}
			mLogger.i(DEBUG_TAG, remainData);
			mLogger.i(DEBUG_TAG, mGameData.toString());
			for (int i = 0; i < 4; i++) {
				mLogger.i(DEBUG_TAG, "P" + (i + 1) + ":" + mMahjManager.getPlayerDatas().get(i).toString());
			}
			mLogger.i(DEBUG_TAG, "<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-<-");
		}
	}
	
	private void updateGameInfoToFile() {
		String message = "->->->->\n";
		String remainData = "[余]";
		for (MahjData data : mGameData.getDatas()) {
			remainData = remainData + data.getIndex() + ",";
		}
		message = message + remainData + "\n" + mGameData.toString() + "\n";
		for (int i = 0; i < 4; i++) {
			message = message + "P" + (i + 1) + ":" + mMahjManager.getPlayerDatas().get(i).toString() + "\n";
		}
		message = message + "<-<-<-<-\n";
		printMessageToFile(message);
	}
	
	private void printMessageToFile(String message) {
		FileLogRecorderManager.getInstance().addMessage(mRoomId, mStartTime, message);
	}
}
