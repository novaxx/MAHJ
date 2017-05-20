package nova.common.game.mahjong;

import nova.common.game.mahjong.util.GameTimer;
import nova.common.game.mahjong.util.TimerCallback;


public class MahjGameStage {
	public interface StageCallBack {
		public void onStageEnd(int stage);
		public boolean hasMatchType();
		public void clearMatchType();
	}
	
	public static final int MAHJ_INIT     = 0;
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
			int timeOut = getTimeOutForStage(mGameStage);
			if (mDuration < timeOut) {
				mDuration++;
				return;
			}
			
			mStageHandler.onStageEnd(mGameStage);
			
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
				if (mStageHandler.hasMatchType()) {
					mGameStage = MATCH_MAHJ_WAIT;
				} else {
					mGameStage = GET_MAHJ_WAIT;
				}
				break;
				
			case MATCH_MAHJ_WAIT:
				if (mStageHandler.hasMatchType()) {
					mGameStage = OUT_MAHJ_WAIT;
				} else {
					mGameStage = GET_MAHJ_WAIT;
				}
				mStageHandler.clearMatchType();
				break;
				
			default:
				break;
			}
			cleanStageTime();
		}
	};
	
	public MahjGameStage() {
		mTimer = new GameTimer(mCallback);
	}
	
    public void start() {
    	if (!mTimer.isRunning()) {
    		cleanStageTime();
    		mTimer.start();
    	}
    }
    
    public void restart() {
    	if (mTimer.isRunning()) {
    		mTimer.stop();
    	}
    	cleanStageTime();
    	mTimer = new GameTimer(mCallback);
    	mTimer.start();
    }
    
    public void stop() {
    	if (mTimer.isRunning()) {
    		mTimer.stop();
    	}
    	cleanStageTime();
    }
    
    public void setStageHandler(StageCallBack handler) {
    	mStageHandler = handler;
    }
    
    private void cleanStageTime() {
    	mDuration = 0;
    }
    
    private int getTimeOutForStage(int stage) {
    	switch (stage) {
		case MATCH_MAHJ_WAIT:
			return 0;

		default:
			break;
		}
    	return 0;
    }
}