package nova.common.game.mahjong.handler;

public class GameLogger {

	public interface LoggerHandler {
		public void info(String tag, String msg);

		public void debug(String tag, String msg);

		public void error(String tag, String msg);
	}

	private static GameLogger mLogger;
	private static LoggerHandler mHandler;

	private GameLogger() {
	}

	public static void create(LoggerHandler handler) {
		if (mLogger == null) {
			mLogger = new GameLogger();
			mHandler = handler;
		}
	}

	public static GameLogger getInstance() {
		if (mLogger == null) {
			create(null);
		}
		return mLogger;
	}

	public void i(String tag, String msg) {
		if (mHandler != null) {
			mHandler.info(tag, msg);
		}
	}
	
	public void d(String tag, String msg) {
		if (mHandler != null) {
			mHandler.debug(tag, msg);
		}
	}

	public void e(String tag, String msg) {
		if (mHandler != null) {
			mHandler.error(tag, msg);
		}
	}
}
