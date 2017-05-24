package nova.common.game.mahjong.data;

import java.util.ArrayList;
import java.util.HashMap;

public class MahjResponeData {

	private class MahjPartData {
		private int latestData;
		private ArrayList<Integer> datas = new ArrayList<Integer>();
		private ArrayList<Integer> matchedatas = new ArrayList<Integer>();
		private ArrayList<Integer> outdatas = new ArrayList<Integer>();
		
		public MahjPartData(MahjGroupData groupData) {
			if (groupData.getLatestData() != null) {
				latestData = groupData.getLatestData().getIndex();
			}
			
			for (int i = 0; i < groupData.getDatas().size(); i++) {
				datas.add(groupData.getDatas().get(i).getIndex());
			}
			
			for (int i = 0; i < groupData.getMatchDatas().size(); i++) {
				matchedatas.add(groupData.getMatchDatas().get(i).getIndex());
			}
			
			for (int i = 0; i < groupData.getOutDatas().size(); i++) {
				outdatas.add(groupData.getOutDatas().get(i).getIndex());
			}
		}
		
		public int getLatestData() {
			return latestData;
		}
		
		public  ArrayList<Integer> getDatas() {
			return datas;
		}
		
		public  ArrayList<Integer> getMatcheDatas() {
			return matchedatas;
		}
		
		public  ArrayList<Integer> getOutDatas() {
			return outdatas;
		}
	}
	
	private int banker;
	private int current;
	private int god;
	private ArrayList<Integer> remainingDatas = new ArrayList<Integer>();
	private HashMap<Integer, MahjPartData> partDatas = new HashMap<Integer, MahjPartData>();
	
	public MahjResponeData(MahjGameData gameData, HashMap<Integer, MahjGroupData> groupDatas) {
		banker = gameData.getBanker();
		current = gameData.getCurrent();
		god = gameData.getGod();
		
		for (int i = 0; i < gameData.getDatas().size(); i++) {
			remainingDatas.add(gameData.getDatas().get(i).getIndex());
		}
		
		for (int i = 0; i < 4; i++) {
			if (groupDatas.get(i) == null) {
				continue;
			}
			
			partDatas.put(i, new MahjPartData(groupDatas.get(i)));
		}
	}
	
	public int getBanker() {
		return banker;
	}
	
	public int getCurrent() {
		return current;
	}
	
	public int getGod() {
		return god;
	}
	
	public ArrayList<Integer> getRemainingDatas() {
		return remainingDatas;
	}
	
	public HashMap<Integer, MahjGroupData> getGroupDatas() {
		HashMap<Integer, MahjGroupData> groupDatas = new HashMap<Integer, MahjGroupData>();
		for (int i = 0; i < 4; i++) {
			if (partDatas.get(i) == null) {
				continue;
			}
			
			MahjPartData partData = partDatas.get(i);
			ArrayList<MahjData> datas = new ArrayList<MahjData>();
			for (int index : partData.getDatas()) {
				datas.add(new MahjData(index));
			}
			MahjGroupData groupData = new MahjGroupData(i, datas);
			groupData.setLatestData(new MahjData(partData.getLatestData()));
			ArrayList<MahjData> outdatas = new ArrayList<MahjData>();
			for (int index : partData.getOutDatas()) {
				outdatas.add(new MahjData(index));
			}
			groupData.setOutDatas(outdatas);
			ArrayList<MahjData> matchdatas = new ArrayList<MahjData>();
			for (int index : partData.getMatcheDatas()) {
				matchdatas.add(new MahjData(index));
			}
			groupData.setMatchDatas(matchdatas);
			groupData.updateGodData(god);
			groupDatas.put(i, groupData);
		}
		
		return groupDatas;
	}
}
