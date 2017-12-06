package nova.common.game.mahjong.handler;

public class FileRecorderManager {
	private static FileRecorderManager mInstance;
	private FileRecorderRunnable mRunnable;
	private String mFilePath;
	
	private FileRecorderManager() {
		mRunnable = new FileRecorderRunnable();
	}
	
	public static FileRecorderManager getInstance() {
		if (mInstance == null) {
			mInstance = new FileRecorderManager();
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
			GameLogger.getInstance().e("FileRecorderManager", "file path is empty !!");
			return;
		}
		
		mRunnable.addMessage(roomId, time, message);
	}
}
