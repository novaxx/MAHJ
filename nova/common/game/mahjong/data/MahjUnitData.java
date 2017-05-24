package nova.common.game.mahjong.data;

import java.util.ArrayList;
import java.util.HashMap;

public class MahjUnitData {

	private ArrayList<MahjData> mDatas = new ArrayList<MahjData>();
	private HashMap<Integer, ArrayList<Integer>> mUnitInfos = new HashMap<Integer, ArrayList<Integer>>();
	private int mGrade;
	
	public void addAll(ArrayList<MahjData> datas) {
		mDatas.addAll(datas);
	}
	
	public void add(MahjData data) {
		mDatas.add(data);
	}
	
	public int size() {
		return mDatas.size();
	}
	
	public int grade() {
		return mGrade;
	}
	
	public void updateUnitDataInfo() {
		updateUnitInfo();
		
		mGrade = mDatas.size();
	}
	
	public ArrayList<Integer> getIndexs() {
		ArrayList<Integer> indexs = new ArrayList<Integer>();
		for (MahjData data : mDatas) {
			indexs.add(data.getIndex());
		}
		return indexs;
	}
	
	public MahjData getOutData() {
		return mDatas.get(0);
	}
	
	/*
	 * 百／十／个
	 * 杆／碰／-
	 */
	public int getMatchType(MahjData data) {
		int gan = 0;
		int peng = 0;
		if (mUnitInfos.get(3) != null && mUnitInfos.get(3).contains(data.getIndex())) {
			gan = 1;
		}
		
		if (mUnitInfos.get(2) != null && mUnitInfos.get(2).contains(data.getIndex())) {
			peng = 1;
		}
		
		return gan * 100 + peng * 10;
	}
	
	public String toString() {
		String message = "datas:";
		for (MahjData data : mDatas) {
			message = message + data.getIndex() + ",";
		}
		message = message + "infos:";
		
		for (int i = 1; i <= 4; i++) {
			if (mUnitInfos.get(i) == null) {
				continue;
			}
			message = message + "[" + i + "]";
			for (Integer index : mUnitInfos.get(i)) {
				message = message + index + " ";
			}
		}
		return message;
	}
	
	private void updateUnitInfo() {
		mUnitInfos.clear();
		int count = 0;
		int index = -1;
		for (int i = 0; i < mDatas.size(); i++) {
			if (index == mDatas.get(i).getIndex()) {
				count++;
			} else {
				index = mDatas.get(i).getIndex();
				count = 1;
			}
			setUnitInfo(count, index);
		}
	}
	
	private void setUnitInfo(int count, int order) {
	    ArrayList<Integer> infoList = mUnitInfos.get(count);
	    if (infoList == null) {
	    	infoList = new ArrayList<Integer>();
	    }
	    infoList.add(order);
	    mUnitInfos.put(count, infoList);
	}
}