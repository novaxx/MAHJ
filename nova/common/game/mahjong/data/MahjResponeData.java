package nova.common.game.mahjong.data;

import java.util.ArrayList;
import java.util.HashMap;

public class MahjResponeData {

	public class MahjPartData {
		private boolean isOuted;
		private int operate;
		private int latestData;
		private ArrayList<Integer> datas = new ArrayList<Integer>();
		private ArrayList<Integer> matcheDatas = new ArrayList<Integer>();
		private ArrayList<Integer> outDatas = new ArrayList<Integer>();
		
		public MahjPartData(MahjGroupData groupData) {
			isOuted = groupData.isOuted();
			operate = groupData.getOperateType();
			
			if (groupData.getLatestData() != null) {
				latestData = groupData.getLatestData().getIndex();
			}
			
			for (int i = 0; i < groupData.getDatas().size(); i++) {
				datas.add(groupData.getDatas().get(i).getIndex());
			}
			
			for (int i = 0; i < groupData.getMatchDatas().size(); i++) {
				matcheDatas.add(groupData.getMatchDatas().get(i).getIndex());
			}
			
			for (int i = 0; i < groupData.getOutDatas().size(); i++) {
				outDatas.add(groupData.getOutDatas().get(i).getIndex());
			}
		}
		
		public boolean isOuted() {
			return isOuted;
		}
		
		public int getOperate() {
			return operate;
		}
		
		public int getLatestData() {
			return latestData;
		}
		
		public  ArrayList<Integer> getDatas() {
			return datas;
		}
		
		public  ArrayList<Integer> getMatcheDatas() {
			return matcheDatas;
		}
		
		public  ArrayList<Integer> getOutDatas() {
			return outDatas;
		}
	}
	
	private int banker;
	private int current;
	private int god;
	private ArrayList<Integer> remainingDatas = new ArrayList<Integer>();
	private HashMap<String, MahjPartData> partDatas = new HashMap<String, MahjPartData>();
	
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
			
			partDatas.put(String.valueOf(i), new MahjPartData(groupDatas.get(i)));
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
	
	public HashMap<String, MahjPartData> getPartDatas() {
		return partDatas;
	}
}
