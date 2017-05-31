package nova.common.game.mahjong.data;

import java.util.ArrayList;
import java.util.HashMap;

import nova.common.game.mahjong.util.MahjConstant;
import nova.common.game.mahjong.util.MahjHandlerUtil;

public class MahjGroupData {
	private ArrayList<MahjData> mDatas = new ArrayList<MahjData>();
	private ArrayList<MahjData> mMatchDatas = new ArrayList<MahjData>();
	private ArrayList<MahjData> mOutDatas = new ArrayList<MahjData>();
	private MahjData mLatestData;
	private int mGodIndex = -1;
	// 正在进行的操作：1吃10碰100杠1000听10000胡
	private int mOperateType;
	
	// 0万1条2筒3东4中5GOD
	private static final int GROUP_ID_MAX = 5;
	private int mPlayerId;
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

	public void updateGodData(int index) {
		mGodIndex = index;
		sortGroupData(mDatas);
		initUnitDatas();
	}

	public void addMatchData(MahjData data, int matchType) {
		if (matchType != MahjConstant.MAHJ_MATCH_CHI && matchType != MahjConstant.MAHJ_MATCH_PENG
				&& matchType != MahjConstant.MAHJ_MATCH_GANG) {
			return;
		}

		mMatchDatas.add(data);
		final int mahjCount = (matchType == MahjConstant.MAHJ_MATCH_PENG ? 2 : 3);

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

	public void setMatchDatas(ArrayList<MahjData> datas) {
		mMatchDatas.clear();
		mMatchDatas.addAll(datas);
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

	public boolean containData(final int index) {
		if (mLatestData.getIndex() == index) {
			return true;
		}
		
		for (MahjData data : mDatas) {
			if (data.getIndex() == index) {
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * @return 万／千／百／十／个 
	 *                         胡／听／杆／碰／吃
	 */
	public int getMatchType() {
		return mMatchType;
	}

	public int updateMatchType(MahjData data) {
		if (data == null) {
			mMatchType = 0;
		} else {
			mMatchType = getMatchTypeForData(data);
		}
		
		return mMatchType;
	}
	
	public int updateMatchTypeForLatestData() {
		int groupId = getGroupIdByData(mLatestData);
		int matchType = 0;
		if (isHuEnable()) {
			matchType += MahjConstant.MAHJ_MATCH_HU;
		}
		
		if (mUnitDatas.get(groupId).isGangEnable(mLatestData)) {
			matchType += MahjConstant.MAHJ_MATCH_GANG;
		}
		
		return matchType;
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
	
	public void setOperateType(int type) {
		mOperateType = type;
	}
	
	public int getOperateType() {
		return mOperateType;
	}
	
	public ArrayList<Integer> getTingDatas() {
		ArrayList<Integer> tingDatas = new ArrayList<Integer>();
		if (getCommonGroupCount(mUnitDatas) > 1) {
			return tingDatas;
		}
		
		ArrayList<Integer> tmpDatas = new ArrayList<Integer>();
		for (int i = 0; i < GROUP_ID_MAX; i++) {
			MahjUnitData unitData = mUnitDatas.get(i);
			if (i < 3 && (unitData == null || unitData.size() <= 0)) {
				continue;
			}
			
			for (int j = 0; j < MahjConstant.MAHJ_ARRS[i].length; j++) {
				if (getCountFromDatas(MahjConstant.MAHJ_ARRS[i][j]) < 4) {
					tmpDatas.add(MahjConstant.MAHJ_ARRS[i][j]);
				}
			}
		}
		if (!tmpDatas.contains(mGodIndex) && getCountFromDatas(mGodIndex) < 4) {
			tmpDatas.add(mGodIndex);
		}
		
		for (int data : tmpDatas) {
			MahjGroupData groupData = copyGroupData();
			groupData.setLatestData(new MahjData(data));
			if (groupData.isHuEnable()) {
				tingDatas.add(data);
			}
		}
		
		return tingDatas;
	}
	
	public boolean isHuEnable() {
		/*
		 * if (dataCount % 3 != 2) { return false; }
		 */

		if (mOperateType != MahjConstant.MAHJ_MATCH_TING) {
			return false;
		}
		
		if (getCommonGroupCount(mUnitDatas) > 1) {
			return false;
		}
		
		final int godCount = mUnitDatas.get(GROUP_ID_MAX) != null ? mUnitDatas.get(GROUP_ID_MAX).size() : 0;
		for (int jiang = 0; jiang < GROUP_ID_MAX; jiang++) {
			if (mUnitDatas.get(jiang) == null || mUnitDatas.get(jiang).size() <= 0) {
				continue;
			}
			
			int needGodCount = 0;
			for (int i = 0; i < GROUP_ID_MAX; i++) {
				if (i == jiang) {
					continue;
				}
				if (mUnitDatas.get(i) == null || mUnitDatas.get(i).size() <= 0) {
					continue;
				}

				needGodCount += getNeedGodCountForUnitData(mUnitDatas.get(i));
				if (godCount < needGodCount) {
					break;
				}
			}
			boolean isHu = MahjHandlerUtil.isHuEnable(mUnitDatas.get(jiang).getIndexs(), godCount - needGodCount);
			if (isHu) {
				return true;
			}
		}
		return false;
	}

	/*
	 * 计算所有普通麻将(不包括风)的组数，用于需要缺门的麻将规则
	 */
	private int getCommonGroupCount(HashMap<Integer, MahjUnitData> unitDatas) {
		int commonGroupCount = 0;
		for (int i = 0; i < 3; i++) {
			if (unitDatas.get(i) != null && unitDatas.get(i).size() > 0) {
				commonGroupCount++;
			}
		}
		return commonGroupCount;
	}

	private int getNeedGodCountForUnitData(MahjUnitData data) {
		return MahjHandlerUtil.getNeedGodCount(data.getIndexs());
	}

	/*
	 * 万／千／百／十／个 胡／听／杆／碰／吃
	 */
	private int getMatchTypeForData(MahjData data) {
		int groupId = getGroupIdByData(data);
		if (mUnitDatas.get(groupId) != null) {
			return mUnitDatas.get(groupId).getMatchType(data);
		}

		return 0;
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

		for (int i = 0; i < GROUP_ID_MAX; i++) {
			if (mUnitDatas.get(i) == null) {
				continue;
			}
			mUnitDatas.get(i).updateUnitDataInfo();
		}

		/*----添加LOG打印----begin----*/
		String message = "P(" + mPlayerId + ")\n";
		for (int i = 0; i < GROUP_ID_MAX; i++) {
			if (mUnitDatas.get(i) == null) {
				continue;
			}
			message = message + i + "-{" + mUnitDatas.get(i).toString() + "}";
		}
		// android.util.Log.e("zhangxx", message);
		/*----添加LOG打印----end----*/
	}

	private void addDataToUnit(MahjData data) {
		int groupId = getGroupIdByData(data);
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

			if (groupId == -1 || mUnitDatas.get(i).grade() < mUnitDatas.get(groupId).grade()) {
				groupId = i;
			}
		}
		
		if (groupId == -1) {
			for (int i = 0; i < GROUP_ID_MAX; i++) {
				if (mUnitDatas.get(i) != null) {
					groupId = i;
				}
			}
		}

		return groupId;
	}
	
	private int getGroupIdByData(MahjData data) {
		if (data.getIndex() == mGodIndex) {
			return GROUP_ID_MAX;
		}  else {
			return data.getColor();
		}
	}
	
	private int getCountFromDatas(int index) {
		int count = 0;
		if (mLatestData != null && mLatestData.getIndex() == index) {
			count++;
		}
		
		for (MahjData data : mMatchDatas) {
			if (data.getIndex() == index) {
				count++;
			}
		}
		
		for (MahjData data : mDatas) {
			if (data.getIndex() == index) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 拷贝GroupData,用于判断听牌，不影响原有数据
	 * @return
	 * 拷贝的groupData
	 */
	private MahjGroupData copyGroupData() {
		MahjGroupData groupData = new MahjGroupData(0, mDatas);
		groupData.updateGodData(mGodIndex);
		groupData.setMatchDatas(mMatchDatas);
		groupData.setOperateType(MahjConstant.MAHJ_MATCH_TING);
		
		return groupData;
	}
}
