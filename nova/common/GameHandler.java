package nova.common;

import java.util.HashMap;

public interface GameHandler {

	public void start(int roomId);
	public void end(int roomId);
	public void handleResult(int roomId, HashMap<Integer, Integer> results);
}
