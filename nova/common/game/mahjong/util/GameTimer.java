package nova.common.game.mahjong.util;

public class GameTimer extends Thread {
	
	private TimerCallback mCallback;
	private boolean mRunning = false;
	
	public GameTimer(TimerCallback callback) {
		mCallback = callback;
	}
	
	@Override
	public void run() {
		while (mRunning) {
			if (mCallback != null) {
				mCallback.handleMessage();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public boolean isRunning() {
		return mRunning;
	}
	
	public void setHandlerCallback(TimerCallback callback) {
		mCallback = callback;
	}
	
	public void startTimer() {
		mRunning = true;
		start();
	}
	
	public void stopTimer() {
		mRunning = false;
		mCallback = null;
	}
}
