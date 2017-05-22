package nova.common.game.mahjong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import nova.common.game.mahjong.data.MahjData;
import nova.common.game.mahjong.data.MahjGroupData;
import nova.common.game.mahjong.util.MahjConstant;

public class MahjManager {

	private ArrayList<MahjData> mMahjDatas = new ArrayList<MahjData>();
	private HashMap<Integer, MahjGroupData> mPlayerGroupDatas = new HashMap<Integer, MahjGroupData>();
	private int mGodData = -1;
	
	public void initDatas() {
		clearData();
		getRandomDatas();
		fillPlayerDatas();
	}
	
	public ArrayList<MahjData> getMahjDatas() {
		return mMahjDatas;
	}
	
	public HashMap<Integer, MahjGroupData> getPlayerDatas() {
		return mPlayerGroupDatas;
	}
	
	public void getLatestData(int playerId) {
		mPlayerGroupDatas.get(playerId).setLatestData(mMahjDatas.get(0));
		mMahjDatas.remove(0);
	}
	
	public void autoOutData(int playerId) {
		MahjData outData = mPlayerGroupDatas.get(playerId).getAutoOutData();
		mPlayerGroupDatas.get(playerId).removeData(outData);
		mPlayerGroupDatas.get(playerId).addOutData(outData);
		for (int i = 0; i < 4; i++) {
			if (playerId == i) {
				continue;
			}
			mPlayerGroupDatas.get(i).updateMatchType(outData);
		}
	}
	
	public boolean hasMatchType() {
		for (int i = 0; i < 4; i++) {
			if (mPlayerGroupDatas.get(i).getMatchType() > 0) {
				return true;
			}
		}
		
		return false;
	}
	
	public void clearMatchType() {
		for (int i = 0; i < 4; i++) {
			mPlayerGroupDatas.get(i).updateMatchType(null);
		}
	}
	
	public int getFirstMatchType(int playerId) {
		int type = mPlayerGroupDatas.get(playerId).getMatchType();
		if ((type % 100) / 10 > 0) {
			// 杆
			return 10;
		} else if ((type % 1000) / 100 > 0) {
			// 碰
			return 100;
		}
		
		return 0;
	}
	
	public int getFirstMatchPlayer(int playerId) {
		int count = 0;
		int player = getNextPlayer(playerId);
		while (count < 2) {
			if (mPlayerGroupDatas.get(player).getMatchType() != 0) {
				break;
			}
			player = getNextPlayer(player);
			count++;
		}
		
		return player;
	}
	
	public void obtainMatchData(int playerId, int currentPlayerId, int matchType) {
		MahjData data = mPlayerGroupDatas.get(currentPlayerId).getLastOutData();
		mPlayerGroupDatas.get(playerId).addMatchData(data, matchType);
		mPlayerGroupDatas.get(currentPlayerId).removeLastOutData();
	}
	
	private void getRandomDatas() {
		Random random = new Random();
		int[] tempData = new int[MahjConstant.MAH_JONG_COUNT];
		System.arraycopy(MahjConstant.MAH_JONG_ELEMENTS, 0, tempData, 0, MahjConstant.MAH_JONG_COUNT);
		int count = 0;
		int position = 0;
		do {
			position = random.nextInt(MahjConstant.MAH_JONG_COUNT - count);
			int index = tempData[position];
			mMahjDatas.add(new MahjData(index));
			count++;
			tempData[position] = tempData[MahjConstant.MAH_JONG_COUNT - count];
		} while (count < MahjConstant.MAH_JONG_COUNT);
	}
	
	public void updateGodData() {
		if (mGodData == -1) {
			Random random = new Random();
			int position = random.nextInt(mMahjDatas.size());
			mGodData = mMahjDatas.get(position).getIndex();
		}
		
		for (int i = 0; i < 4; i++) {
			mPlayerGroupDatas.get(i).updateGodData(mGodData);
		}
	}
	
	public int getGodData() {
		if (mGodData == -1) {
			updateGodData();
		}
		return mGodData;
	}
	
	private void clearData() {
		mGodData = -1;
		mMahjDatas.clear();
		mPlayerGroupDatas.clear();
	}
	
	private void fillPlayerDatas() {
		initPlayerDatas();
		
		for (int i = 0; i < 4; i++) {
			fillPlayerDataFromTotalData(i);
		}
	}
	
	private void fillPlayerDataFromTotalData(int playerId) {
		ArrayList<MahjData> datas = new ArrayList<MahjData>();
		for (int i = 0; i < mMahjDatas.size(); i++) {
			if (datas.size() >= 13) {
				break;
			}
			
			datas.add(mMahjDatas.get(i));
			mMahjDatas.remove(i);
		}
		mPlayerGroupDatas.put(playerId, new MahjGroupData(playerId, datas));
	}
	
	private void initPlayerDatas() {
		if (mPlayerGroupDatas.size() > 0) {
			mPlayerGroupDatas.clear();
		}
	}
	
	private int getNextPlayer(int playerId) {
		int player = playerId + 1;
		if (player >= 4) {
			player = player - 4;
		}
		
		return player;
	}
}
