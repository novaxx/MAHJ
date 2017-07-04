package nova.common.game.mahjong.data;

import java.util.ArrayList;
import java.util.HashMap;

import nova.common.game.mahjong.util.MahjConstant;

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
		sortUnitData(mDatas);
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
	
	public boolean isGangEnable(MahjData data) {
		if (mUnitInfos.get(4) != null && mUnitInfos.get(4).contains(data.getIndex())) {
			return true;
		}
		
		return false;
	}
	
	public ArrayList<Integer> get4Combine() {
		return mUnitInfos.get(4);
	}
	
	/*
	 * 百／十／个
	 * 杆／碰／-
	 */
	public int getMatchType(MahjData data) {
		int type = 0;
		if (mUnitInfos.get(3) != null && mUnitInfos.get(3).contains(data.getIndex())) {
			type = type | MahjConstant.MAHJ_MATCH_GANG;
		}
		
		if (mUnitInfos.get(2) != null && mUnitInfos.get(2).contains(data.getIndex())) {
			type = type | MahjConstant.MAHJ_MATCH_PENG;
		}
		
		return type;
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
	
	/**
	 * 从大到小排序
	 */
	private void sortUnitData(ArrayList<MahjData> datas) {
		for (int i = 0; i < datas.size(); i++) {
			// 从第i+1为开始循环数组
			for (int j = i + 1; j < datas.size(); j++) {
				// 如果前一位比后一位小，那么就将两个数字调换
				// 这里是按降序排列
				// 如果你想按升序排列只要改变符号即可
				if (datas.get(j).getIndex() > datas.get(i).getIndex()) {
					MahjData tem = datas.get(i);
					datas.set(i, datas.get(j));
					datas.set(j, tem);
				}
			}
		}
	}
}