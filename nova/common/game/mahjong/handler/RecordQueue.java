package nova.common.game.mahjong.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RecordQueue {
	private Queue<RecordRequest> mMessageQueue;
	private boolean mIsRunning = false;
	private PrintStream mPrintStream;

	public RecordQueue(ConcurrentLinkedQueue<RecordRequest> concurrentLinkedQueue) {
		this.mMessageQueue = concurrentLinkedQueue;
	}

	public Queue<RecordRequest> getRequestQueue() {
		return this.mMessageQueue;
	}

	public void setRequestQueue(Queue<RecordRequest> requestQueue) {
		this.mMessageQueue = requestQueue;
	}

	public void clear() {
		this.mMessageQueue.clear();
		this.mMessageQueue = null;
	}

	public int size() {
		return this.mMessageQueue != null ? this.mMessageQueue.size() : 0;
	}

	public boolean add(RecordRequest message) {
		if (mPrintStream == null) {
			try {
				mPrintStream = new PrintStream(new FileOutputStream(new File(FileRecorderManager.getInstance().getFilePath()
						+ message.getRoom() + "_" + message.getTime() + ".txt")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return this.mMessageQueue.add(message);
	}

	public void setRunning(boolean running) {
		this.mIsRunning = running;
	}

	public boolean isRunning() {
		return this.mIsRunning;
	}
	
	public PrintStream getPrint() {
		return mPrintStream;
	}
}
