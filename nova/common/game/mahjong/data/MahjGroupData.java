package nova.common.game.mahjong.data;

import java.util.ArrayList;
import java.util.HashMap;

public class MahjGroupData {
	private int mPlayerId;
	private ArrayList<MahjData> mDatas = new ArrayList<MahjData>();
	private ArrayList<MahjData> mMatchDatas = new ArrayList<MahjData>();
	private MahjData mLatestData;
	private ArrayList<MahjData> mOutDatas = new ArrayList<MahjData>();
	private HashMap<Integer, MahjUnitData> mUnitDatas = new HashMap<Integer, MahjUnitData>();
	private int mMatchType;
	
	public MahjGroupData(int playerId, ArrayList<MahjData> datas) {
		mPlayerId = playerId;
		mDatas = datas;
		sortGroupData(mDatas);
		initUnitDatas();
	}
	
	public ArrayList<MahjData> getDatas() {
		return mDatas;
	}
	
	public void addMatchData(MahjData data, int matchType) {
		mMatchDatas.add(data);
		final int mahjCount = matchType == 100 ? 2 : 3;
		
		for (int i = 0; i < mahjCount; i++) {
			for (int j = 0; j < mDatas.size(); j++) {
				if (mDatas.get(j).getIndex() == data.getIndex()) {
					mMatchDatas.add(mDatas.get(j));
					mDatas.remove(j);
					break;
				}
			}
		}
		
		sortGroupData(mMatchDatas);
		sortGroupData(mDatas);
		initUnitDatas();
	}
	
	public void setLatestData(MahjData data) {
		mLatestData = data;
		initUnitDatas();
	}
	
	public MahjData getLatestData() {
		return mLatestData;
	}
	
	public ArrayList<MahjData> getMatchDatas() {
		return mMatchDatas;
	}
	
	public ArrayList<MahjData> getOutDatas() {
		return mOutDatas;
	}
	
	public void setOutDatas(ArrayList<MahjData> datas) {
		mOutDatas.removeAll(mOutDatas);
		mOutDatas.addAll(datas);
	}
	
	public void addOutData(MahjData data) {
		mOutDatas.add(data);
	}
	
	public MahjData getLastOutData() {
		return mOutDatas.get(mOutDatas.size() - 1);
	}
	
	public void removeLastOutData() {
		mOutDatas.remove(mOutDatas.size() - 1);
	}
	
	public MahjData getAutoOutData() {
		int groupId = getAutoOutGroupId();
		MahjData data = mUnitDatas.get(groupId).getOutData();
		return data;
	}
	
	public int getMatchType() {
		return mMatchType;
	}
	
	public void updateMatchType(MahjData data) {
		if (data == null) {
			mMatchType = 0;
		} else {
			mMatchType = getMatchTypeForData(data);
		}
	}
	
	/**
	 * 
	 * @param data : outData
	 * @return
	 * 个位: 不要
	 * 十位: 杆
	 * 百位：碰
	 * 千位：吃
	 * 万位：胡
	 */
	public int getMatchTypeForData(MahjData data) {
		int groupId = data.getColor();
		if (mUnitDatas.get(groupId) != null) {
			return mUnitDatas.get(groupId).getMatchType(data);
		}
		
		return 0;
	}
	
	public boolean removeData(MahjData data) {
		boolean isSuccess;
		if (mLatestData != null && mLatestData.getIndex() == data.getIndex()) {
			mLatestData = null;
			isSuccess = true;
		} else {
			isSuccess = mDatas.remove(data);
		}
		
		updateGroupData();
		
		return isSuccess;
	}
	
	private void updateGroupData() {
		if (mLatestData != null) {
			mDatas.add(mLatestData);
			mLatestData = null;
		}
		
		sortGroupData(mDatas);
		initUnitDatas();
	}
	
	private void sortGroupData(ArrayList<MahjData> datas) {
		for (int i = 0; i < datas.size(); i++) {
			// 从第i+1为开始循环数组
			for (int j = i + 1; j < datas.size(); j++) {
				// 如果前一位比后一位小，那么就将两个数字调换
				// 这里是按降序排列
				// 如果你想按升序排列只要改变符号即可
				if (compareCard(datas.get(i), datas.get(j))) {
					MahjData tem = datas.get(i);
					datas.set(i, datas.get(j));
					datas.set(j, tem);
				}
			}
		}
	}
	
	private boolean compareCard(MahjData data1, MahjData data2) {
		return data2.getIndex() > data1.getIndex();
	}
	
	private void initUnitDatas() {
		if (mUnitDatas.size() > 0) {
			mUnitDatas.clear();
		}
		
		if (mLatestData != null) {
			addDataToUnit(mLatestData);
		}
		
		for (MahjData data : mDatas) {
			addDataToUnit(data);
		}
		
		String message = "P(" + mPlayerId + ")\n";
		for (int i = 0; i < mUnitDatas.size(); i++) {
			if (mUnitDatas.get(i) == null) {
				continue;
			}
			mUnitDatas.get(i).updateUnitInfo();
			message = message + i + "-{" + mUnitDatas.get(i).toString() + "}"; 
		}
		// android.util.Log.e("zhangxx", message);
	}
	
	private void addDataToUnit(MahjData data) {
		int groupId = data.isGod() ? 5 : data.getColor();
		if (mUnitDatas.get(groupId) == null) {
			mUnitDatas.put(groupId, new MahjUnitData());
		}
		mUnitDatas.get(groupId).add(data);
	}
	
	private int getAutoOutGroupId() {
	    int groupId = -1;
	    for (int i = 0; i < 3; i++) {
	    	if (mUnitDatas.get(i) == null) {
	    		continue;
	    	}
	    	
	    	if (groupId == -1 || 
	    			mUnitDatas.get(i).size() < mUnitDatas.get(groupId).size()) {
	    		groupId = i;
	    	}
	    }
	    
	    return groupId;
	}
}
