package nova.common.game.mahjong.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import nova.common.game.mahjong.data.MahjData;
import nova.common.game.mahjong.data.MahjGameData;
import nova.common.game.mahjong.data.MahjGroupData;
import nova.common.game.mahjong.data.MahjResponeData;
import nova.common.game.mahjong.handler.MahjGameHandler;
import nova.common.game.mahjong.util.GameTimer;
import nova.common.game.mahjong.util.TimerCallback;

/**
 * Created by zhangxx on 17-12-5.
 */

public class TestRecordFileManager {
	private MahjGameHandler mHandler;
	private String mFilePath;
	private LinkedBlockingQueue<MahjResponeData> mRecordQueues = new LinkedBlockingQueue<MahjResponeData>();

	private TimerCallback mCallback = new TimerCallback() {

		@Override
		public void handleMessage() {
			try {
				if (!mRecordQueues.isEmpty()) {
					mHandler.onGameInfoChange(0, mRecordQueues.take());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	public TestRecordFileManager(MahjGameHandler handler) {
		mHandler = handler;
	}

	public void setFilePath(String path) {
		mFilePath = path;
	}

	public void start() {
		if (mFilePath == null || mFilePath.isEmpty()) {
			GameLogger.getInstance().e("TestRecordFileManager", "file path is empty !!");
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				analyzeRecordFromFile();
			}
		}).start();

		new GameTimer(mCallback).startTimer();
	}

	private void analyzeRecordFromFile() {
		File file = new File(mFilePath);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			boolean isRecord = false;
			ArrayList<String> records = new ArrayList<String>();
			String line = null;
			// 一次读入一行，直到读入null为文件结束
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("->->->->")) {
					isRecord = true;
					records.clear();
					continue;
				} else if (line.startsWith("<-<-<-<-")) {
					isRecord = false;
					mRecordQueues.put(getDataFromRecord(records));
					continue;
				}

				if (isRecord) {
					records.add(line);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}

	}

	private MahjResponeData getDataFromRecord(ArrayList<String> records) {
		MahjGameData gameData = new MahjGameData();
		HashMap<Integer, MahjGroupData> groupDatas = new HashMap<Integer, MahjGroupData>();
		for (int i = 0; i < records.size(); i++) {
			String record = records.get(i);
			if (i == 0) {
				gameData.setDatas(getRemainDataFromRecord(record));
			} else if (i == 1) {
				updateGameDataFromRecord(gameData, record);
			} else {
				// P0:[OUT]true;[OT]0;[LD]-1;[MD];[DD]43,42,33,19,15,14,14,13,6,4,2,2,1,;[OD]29,28,27,24,
				int index = Integer.valueOf(record.substring(1, 2));
				groupDatas.put(index, getGroupDataFromRecord(index, record));
			}
		}
		return new MahjResponeData(gameData, groupDatas);
	}

	/**
	 *
	 * record格式: [RD] RemainData [RD]11,4,27,14,2,41,26,4,9,25,17,8,21,41,7,12
	 *
	 */
	private ArrayList<MahjData> getRemainDataFromRecord(String record) {
		ArrayList<MahjData> datas = new ArrayList<MahjData>();
		String[] ss = record.substring(record.indexOf("]") + 1).split(",");
		for (String s : ss) {
			if (s.isEmpty()) {
				continue;
			}
			datas.add(new MahjData(Integer.valueOf(s)));
		}
		return datas;
	}

	/**
	 * record格式(GameData): [RS] RemainSize [BA] Banker [WI] Winner [GO] God [CU]
	 * CurrentPlayer [LO] LastOutPlayer [RS]69;[BA]0;[WI]-1;[GO]3;[CU]2;[LO]2
	 */
	private void updateGameDataFromRecord(MahjGameData data, String record) {
		String[] ss = record.split(";");
		for (String s : ss) {
			int value = Integer.valueOf(s.substring(s.indexOf("]") + 1));
			if (s.startsWith("[RS]")) {

			} else if (s.startsWith("[BA]")) {
				data.setBanker(value);
			} else if (s.startsWith("[WI]")) {
				data.setWinner(value);
			} else if (s.startsWith("[GO]")) {
				data.setGod(value);
			} else if (s.startsWith("[CU]")) {
				data.setCurrent(value);
			} else if (s.startsWith("[LO]")) {
				data.setLastout(value);
			}
		}
	}

	/**
	 * record格式(GroupData); [OUT] out [OT] OperateType [LD] LastestData [MD]
	 * MatchDatas [DD] datas [OD] OutDatas
	 * P0:[OUT]true;[OT]0;[LD]-1;[MD];[DD]43,42,33,19,15,14,14,13,6,4,2,2,1,;[OD
	 * ]29,28,27,24,
	 */
	private MahjGroupData getGroupDataFromRecord(int index, String record) {
		MahjGroupData groupData = new MahjGroupData(index, new ArrayList<MahjData>());
		String[] ss = record.substring(record.indexOf(":") + 1).split(";");
		for (String s : ss) {
			String value = s.substring(s.indexOf("]") + 1);
			if (s.startsWith("[OUT]")) {
				groupData.setOuted(Boolean.valueOf(value));
			} else if (s.startsWith("[OT]")) {
				groupData.setOperateType(Integer.valueOf(value));
			} else if (s.startsWith("[LD]")) {
				if (Integer.valueOf(value) > 0) {
					groupData.setLatestData(new MahjData(Integer.valueOf(value)));
				}
			} else if (s.startsWith("[MD]")) {
				groupData.setMatchDatas(getMahjDatasFromRecord(value));
			} else if (s.startsWith("[DD]")) {
				groupData.getDatas().addAll(getMahjDatasFromRecord(value));
			} else if (s.startsWith("[OD]")) {
				groupData.setOutDatas(getMahjDatasFromRecord(value));
			}
		}

		return groupData;
	}

	private ArrayList<MahjData> getMahjDatasFromRecord(String record) {
		ArrayList<MahjData> datas = new ArrayList<MahjData>();
		String[] ss = record.split(",");
		for (String s : ss) {
			if (s.isEmpty()) {
				continue;
			}
			datas.add(new MahjData(Integer.valueOf(s)));
		}
		return datas;
	}
}
