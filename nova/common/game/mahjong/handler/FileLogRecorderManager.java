package nova.common.game.mahjong.handler;

public class FileLogRecorderManager {
	private static FileLogRecorderManager mInstance;
	private FileLogRecorderRunnable mRunnable;
	private String mFilePath;
	
	private FileLogRecorderManager() {
		mRunnable = new FileLogRecorderRunnable();
	}
	
	public static FileLogRecorderManager getInstance() {
		if (mInstance == null) {
			mInstance = new FileLogRecorderManager();
		}
		
		return mInstance;
	}
	
	public void setFilePath(String path) {
		mFilePath = path;
	}
	
	public String getFilePath() {
		return mFilePath;
	}
	
	public void startRecord() {
		new Thread(mRunnable).start();
	}
	
	public void addMessage(int roomId, String time, String message) {
		if (mFilePath == null || mFilePath.isEmpty()) {
			GameLogger.getInstance().e("FileLogRecorderManager", "file path is empty !!");
			return;
		}
		
		mRunnable.addMessage(roomId, time, message);
	}
}
