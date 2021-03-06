package nova.common.game.mahjong;

import nova.common.game.mahjong.handler.GameLogger;
import nova.common.game.mahjong.util.GameTimer;
import nova.common.game.mahjong.util.MahjConstant;
import nova.common.game.mahjong.util.TimerCallback;

public class MahjGameStage {
	public interface StageCallBack {
		public void onStageEnd(int stage);
		public boolean hasMatchType();
		public int getOperateType();
		public void onDataOutEnd(boolean isMatched);
		public void onTimeChange();
		public boolean isAutoOperator();
		public boolean hasNoMahj();
	}

	public static final int MAHJ_INIT = 0;
	public static final int GET_MAHJ_GOD = 1;
	public static final int GET_MAHJ_WAIT = 2;
	public static final int OUT_MAHJ_WAIT = 3;
	public static final int MATCH_MAHJ_WAIT = 4;
	public static final int MAHJ_END = 5;

	private GameTimer mTimer;
	private StageCallBack mStageHandler;
	// 倒计时
	private int mDuration = 0;
	private int mGameStage = 0;

	private TimerCallback mCallback = new TimerCallback() {

		@Override
		public void handleMessage() {
			mStageHandler.onTimeChange();
			int timeOut = getTimeOutForStage(mGameStage);
			if (mDuration < timeOut) {
				mDuration++;
				return;
			}
			mStageHandler.onStageEnd(mGameStage);
			updateStage();
		}
	};

	public MahjGameStage() {
		// mTimer = new GameTimer(mCallback);
	}

	public void start() {
		if (mTimer != null && mTimer.isRunning()) {
			GameLogger.getInstance().e("MahjGameStage", "start error : game is started!!");
			return;
		}

		cleanStageTime();
		mTimer = new GameTimer(mCallback);
		mTimer.startTimer();
	}

	public void restart() {
		if (mTimer != null && mTimer.isRunning()) {
			mTimer.stopTimer();
			mTimer = null;
		}
		
		cleanStageTime();
		mTimer = new GameTimer(mCallback);
		mTimer.startTimer();
	}

	public void stop() {
		if (mTimer != null) {
			mTimer.stopTimer();
			mTimer = null;
		}
		mGameStage = 0;
		cleanStageTime();
	}

	public void setStageHandler(StageCallBack handler) {
		mStageHandler = handler;
	}

	public void updateStage() {
		switch (mGameStage) {
		case MAHJ_INIT:
			mGameStage = GET_MAHJ_GOD;
			break;

		case GET_MAHJ_GOD:
			mGameStage = GET_MAHJ_WAIT;
			break;

		case GET_MAHJ_WAIT:
			mGameStage = OUT_MAHJ_WAIT;
			break;

		case OUT_MAHJ_WAIT:
			if ((mStageHandler.getOperateType() & MahjConstant.MAHJ_MATCH_HU) == MahjConstant.MAHJ_MATCH_HU
			|| mStageHandler.hasNoMahj()) {
				mGameStage = MAHJ_END;
			} else if ((mStageHandler.getOperateType() & MahjConstant.MAHJ_MATCH_GANG) == MahjConstant.MAHJ_MATCH_GANG) {
				mGameStage = GET_MAHJ_WAIT;
				mStageHandler.onDataOutEnd(true);
			} else if (mStageHandler.hasMatchType()) {
				mGameStage = MATCH_MAHJ_WAIT;
			} else {
				mGameStage = GET_MAHJ_WAIT;
				mStageHandler.onDataOutEnd(false);
			}
			break;

		case MATCH_MAHJ_WAIT:
			if ((mStageHandler.getOperateType() & MahjConstant.MAHJ_MATCH_GANG) == MahjConstant.MAHJ_MATCH_GANG) {
				mGameStage = GET_MAHJ_WAIT;
				mStageHandler.onDataOutEnd(true);
			} else if ((mStageHandler.getOperateType() & MahjConstant.MAHJ_MATCH_PENG) == MahjConstant.MAHJ_MATCH_PENG) {
				mGameStage = OUT_MAHJ_WAIT;
				mStageHandler.onDataOutEnd(true);
			} else {
				mGameStage = GET_MAHJ_WAIT;
				mStageHandler.onDataOutEnd(false);
			}
			break;
			
		case MAHJ_END:
			break;

		default:
			break;
		}
		cleanStageTime();
	}

	private void cleanStageTime() {
		mDuration = 0;
	}

	private int getTimeOutForStage(int stage) {
		switch (stage) {
		case MATCH_MAHJ_WAIT:
			return 3;
		case OUT_MAHJ_WAIT:
			if (mStageHandler.isAutoOperator()) {
				return 0;
			} else {
				return 20;
			}
		default:
			break;
		}
		return 0;
	}
}