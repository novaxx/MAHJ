package nova.common.game.mahjong.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public class FileRecorderRunnable implements Runnable {
	private GameLogger mLogger = GameLogger.getInstance();
	private static final String TAG = "FileRecorderRunnable";
	private final Map<String, RecordQueue> mSessionMsgQ;
	private Executor messageExecutor;
	private boolean mIsRunning;
	private long mSleepTime;

	public FileRecorderRunnable() {
		this.mSessionMsgQ = new ConcurrentHashMap<String, RecordQueue>();
		this.messageExecutor = new Executor() {

			@Override
			public void execute(Runnable command) {
				command.run();
			}
		};
		this.mIsRunning = true;
		this.mSleepTime = 200L;
	}

	public void setMessageExecutor(Executor messageExecutor) {
		this.messageExecutor = messageExecutor;
	}

	public void setSleepTime(long sleepTime) {
		this.mSleepTime = sleepTime;
	}

	public void addMessageQueue(String roomId_time, RecordQueue messageQueue) {
		this.mSessionMsgQ.put(roomId_time, messageQueue);
	}

	public void removeMessageQueue(String roomId_time) {
		RecordQueue queue = (RecordQueue) this.mSessionMsgQ.remove(roomId_time);
		if (queue != null) {
			queue.clear();
		}
	}

	public void addMessage(Integer roomId, String time, String message) {
		try {
			String roomId_time = roomId + "_" + time;
			RecordQueue messageQueue = (RecordQueue) this.mSessionMsgQ.get(roomId_time);
			if (messageQueue == null) {
				messageQueue = new RecordQueue(new ConcurrentLinkedQueue<RecordRequest>());
				this.mSessionMsgQ.put(roomId_time, messageQueue);
			}

			messageQueue.add(new RecordRequest(roomId, time, message));
		} catch (Exception e) {
			mLogger.e(TAG, e.toString());
		}
	}

	public void run() {
		while (this.mIsRunning) {
			try {
				for (RecordQueue messageQueue : mSessionMsgQ.values()) {
					if ((messageQueue != null) && (messageQueue.size() > 0) && (!messageQueue.isRunning())) {
						MessageWorker messageWorker = new MessageWorker(messageQueue);

						this.messageExecutor.execute(messageWorker);
					}
				}
			} catch (Exception e) {
				mLogger.e(TAG, e.toString());
			}
			try {
				Thread.sleep(this.mSleepTime);
			} catch (InterruptedException e) {
				mLogger.e(TAG, e.toString());
			}
		}
	}

	public void stop() {
		this.mIsRunning = false;
	}

	public RecordQueue getUserMessageQueue(String roomId_time) {
		return (RecordQueue) this.mSessionMsgQ.get(roomId_time);
	}

	private final class MessageWorker implements Runnable {
		private final RecordQueue messageQueue;
		private RecordRequest message;

		private MessageWorker(RecordQueue messageQueue) {
			messageQueue.setRunning(true);
			this.messageQueue = messageQueue;
			this.message = ((RecordRequest) messageQueue.getRequestQueue().poll());
		}

		public void run() {
			try {
				handMessageQueue();
			} catch (Exception e) {
				mLogger.e(TAG, e.toString());
			} finally {
				this.messageQueue.setRunning(false);
			}
		}

		private void handMessageQueue() {
			String body = this.message.getBody();
			messageQueue.getPrint().append(body);
			messageQueue.getPrint().append("\n");
			
			// 一局游戏结束移除队列
			if (body.contains("GAME OVER")) {
				removeMessageQueue(this.message.getRoomId_Time());
			}
		}
	}
}
