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
	private boolean mIsOuted = false;
	private HashMap<Integer, MahjUnitData> mUnitDatas = new HashMap<Integer, MahjUnitData>();
	private int mMatchType;

	public MahjGroupData(int playerId, ArrayList<MahjData> datas) {
		mPlayerId = playerId;
		mDatas = datas;
		updateGroupData();
	}

	public ArrayList<MahjData> getDatas() {
		return mDatas;
	}

	public void updateGodData(int index) {
		mGodIndex = index;
		updateGroupData();
	}
	
	public int getGodIndex() {
		return mGodIndex;
	}

	public void addMatchData(MahjData data, int matchType) {
		if (matchType != MahjConstant.MAHJ_MATCH_CHI && matchType != MahjConstant.MAHJ_MATCH_PENG
				&& matchType != MahjConstant.MAHJ_MATCH_GANG) {
			return;
		}
		operateData(data.getIndex(), matchType, false);
	}
	
	public void operateGangData(int index) {
		operateData(index, MahjConstant.MAHJ_MATCH_GANG, true);
	}

	public void setLatestData(MahjData data) {
		moveLatestDataToDatas();
		mLatestData = data;
		updateGroupData();
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
		if (!mIsOuted) {
			setOuted(true);
		}
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
		if (mLatestData != null && mLatestData.getIndex() == index) {
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
	
	public int updateMatchTypeForGetMahj() {
		int matchType = 0;
		if (isHuEnable()) {
			matchType = matchType | MahjConstant.MAHJ_MATCH_HU;
		}
		
		if (getGangListFromDatas().size() > 0) {
			matchType = matchType | MahjConstant.MAHJ_MATCH_GANG;
		}
		
		int dataSizeInMatchs = 0;
		for (MahjData data : mMatchDatas) {
			if (mLatestData.getIndex() == data.getIndex()) {
				dataSizeInMatchs++;
			}
		}
		
		if (dataSizeInMatchs == 3) {
			matchType = matchType | MahjConstant.MAHJ_MATCH_GANG;
		}
		
		return matchType;
	}

	public boolean removeData(MahjData data) {
		boolean isSuccess = false;
		if (mLatestData != null && mLatestData.getIndex() == data.getIndex()) {
			mLatestData = null;
			isSuccess = true;
		} else {
			for (int i = 0; i < mDatas.size(); i++) {
				if (mDatas.get(i).getIndex() == data.getIndex()) {
					mDatas.remove(i);
					isSuccess = true;
					break;
				}
			}
		}

		moveLatestDataToDatas();
		updateGroupData();

		return isSuccess;
	}
	
	public boolean isOuted() {
		return mIsOuted;
	}
	
	public void setOuted(boolean isOut) {
		mIsOuted = isOut;
	}
	
	public void setOperateType(int type) {
		mOperateType = mOperateType | type;
	}
	
	/**
	 * 没有听和胡时，清空operateType
	 * 有听和胡时，清掉其他operateType, 保留听和胡
	 */
	public void clearOperateType() {
		if (mOperateType < MahjConstant.MAHJ_MATCH_TING) {
			mOperateType = 0;
		} else {
			mOperateType = mOperateType & (MahjConstant.MAHJ_MATCH_TING + MahjConstant.MAHJ_MATCH_HU);
		}
	}
	
	public int getOperateType() {
		return mOperateType;
	}
	
	public ArrayList<Integer> getGangListFromDatas() {
		ArrayList<Integer> gangList = new ArrayList<>();
		for (int i = 0; i <= GROUP_ID_MAX; i++) {
			if (mUnitDatas.get(i) == null) {
				continue;
			}
			
			if (mUnitDatas.get(i).get4Combine() != null && mUnitDatas.get(i).get4Combine().size() > 0) {
				gangList.addAll(mUnitDatas.get(i).get4Combine());
			}
		}
		return gangList;
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
		int dataCount = mDatas.size();
		if (mLatestData != null && mLatestData.getIndex() > 0) {
			dataCount += 1;
		}
		/**
		 * 牌数不对(不包括碰，杆)，不能胡牌，胡牌牌数：14, 11,  8, 5, 2
		 */
		if (dataCount % 3 != 2) {
			return false;
		}

		// 不听牌不能胡牌，天胡为特殊情况
		if ((mOperateType & MahjConstant.MAHJ_MATCH_TING) != MahjConstant.MAHJ_MATCH_TING && mIsOuted) {
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
			boolean isHu = MahjHandlerUtil.isHuEnable(mUnitDatas.get(jiang).getIndexs(), mGodIndex, godCount - needGodCount);
			if (isHu) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<ArrayList<String>> getHuList() {
		ArrayList<ArrayList<String>> huList = new ArrayList<ArrayList<String>>();
		final int godCount = mUnitDatas.get(GROUP_ID_MAX) != null ? mUnitDatas.get(GROUP_ID_MAX).size() : 0;
		for (int jiang = 0; jiang < GROUP_ID_MAX; jiang++) {
			ArrayList<String> orders = new ArrayList<String>();
			if (mUnitDatas.get(jiang) == null || mUnitDatas.get(jiang).size() <= 0) {
				continue;
			}
			
			updateOrderForMatchDatas(orders);
			
			int needGodCount = 0;
			for (int i = 0; i < GROUP_ID_MAX; i++) {
				if (i == jiang) {
					continue;
				}
				if (mUnitDatas.get(i) == null || mUnitDatas.get(i).size() <= 0) {
					continue;
				}

				needGodCount += getNeedGodCountForUnitData(mUnitDatas.get(i), orders);
				if (godCount < needGodCount) {
					break;
				}
			}
			boolean isHu = MahjHandlerUtil.isHuEnable(mUnitDatas.get(jiang).getIndexs(), mGodIndex, godCount - needGodCount, orders);
			if (isHu) {
				huList.add(orders);
			}
		}
		return huList;
	}
	
	private void updateOrderForMatchDatas(ArrayList<String> orders) {
		int index = -1;
		String order = "";
		for (MahjData data : mMatchDatas) {
			if (index == -1) {
				index = data.getIndex();
				order = order + index + ",";
			} else if (index != data.getIndex()) {
				orders.add(order);
				index = data.getIndex();
				order = index + ",";
			} else {
				order = order + index + ",";
			}
		}
		orders.add(order);
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
		return MahjHandlerUtil.getNeedGodCount(mGodIndex, data.getIndexs(), null);
	}
	
	private int getNeedGodCountForUnitData(MahjUnitData data, ArrayList<String> orders) {
		return MahjHandlerUtil.getNeedGodCount(mGodIndex, data.getIndexs(), orders);
	}

	/*
	 * 万／千／百／十／个 胡／听／杆／碰／吃
	 */
	private int getMatchTypeForData(MahjData data) {
		if (!isDataMatchAllowed(data)) {
			return 0;
		}
		
		int groupId = getGroupIdByData(data);
		if (mUnitDatas.get(groupId) != null) {
			int matchType = mUnitDatas.get(groupId).getMatchType(data);
			if ((mOperateType & MahjConstant.MAHJ_MATCH_TING) == MahjConstant.MAHJ_MATCH_TING) {
				// 听牌之后不能再吃/碰
				matchType = matchType & (MahjConstant.MAHJ_MATCH_GANG + MahjConstant.MAHJ_MATCH_TING + MahjConstant.MAHJ_MATCH_HU);
			}
			return matchType;
		}

		return 0;
	}
	
	/**
	 * 当前的牌是否允许碰杆
	 * 已有其他花色牌碰杆，不允许
	 * 其他已碰杆花色牌为赖子，允许
	 */
	private boolean isDataMatchAllowed(MahjData data) {
		boolean isAllowed = true;
		
		// 东/南/西/中/发/白/赖子 始终允许
		if (getGroupIdByData(data) > 2) {
			return isAllowed;
		}
		
		for (MahjData tmpData : mMatchDatas) {
			if (tmpData.getIndex() != mGodIndex
					&& getGroupIdByData(tmpData) <= 2
					&& getGroupIdByData(tmpData) != getGroupIdByData(data)) {
				isAllowed = false;
				break;
			}
		}
		
		return isAllowed;
	}

	private void moveLatestDataToDatas() {
		if (mLatestData != null) {
			mDatas.add(mLatestData);
			mLatestData = null;
		}
	}
	
	private void updateGroupData() {
		sortGroupData(mMatchDatas);
		sortGroupData(mDatas);
		initUnitDatas();
	}

	private void sortGroupData(ArrayList<MahjData> datas) {
		for (int i = 0; i < datas.size(); i++) {
			// 从第i+1为开始循环数组
			for (int j = i + 1; j < datas.size(); j++) {
				// 如果后一位比前一位小，那么就将两个数字调换
				// 这里是按升序排列
				// 如果你想按降序排列只要改变符号即可
				if (compareCard(datas.get(i), datas.get(j))) {
					MahjData tem = datas.get(i);
					datas.set(i, datas.get(j));
					datas.set(j, tem);
				}
			}
		}
	}

	private boolean compareCard(MahjData data1, MahjData data2) {
		return data2.getIndex() < data1.getIndex();
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
		// GameLogger.getInstance().e("zhangxx", message);
		/*----添加LOG打印----end----*/
	}

	private void addDataToUnit(MahjData data) {
		if (data == null || data.getIndex() <= 0) {
			return;
		}
		
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
	
	/**
	 *   处理 听/碰/杠
	 * @param index 
	 * @param operateType
	 * @param isOwnGang 是否暗杠
	 */
	private void operateData(int index, int operateType, boolean isOwnGang) {
		if (!isOwnGang) {
			mMatchDatas.add(new MahjData(index));
		}
		
		int mahjCount = 0;
		if (operateType == MahjConstant.MAHJ_MATCH_GANG && isOwnGang) {
			mahjCount = 4;
		} else if (operateType == MahjConstant.MAHJ_MATCH_GANG) {
			mahjCount = 3;
		} else if (operateType == MahjConstant.MAHJ_MATCH_PENG) {
			mahjCount = 2;
		}

		for (int i = 0; i < mahjCount; i++) {
			if (mLatestData != null && mLatestData.getIndex() == index) {
				// 新拿的牌为杆/碰的牌，先把这张牌杆掉
				mMatchDatas.add(mLatestData);
				mLatestData = null;
				continue;
			}
			
			for (int j = 0; j < mDatas.size(); j++) {
				if (mDatas.get(j).getIndex() == index) {
					mMatchDatas.add(mDatas.get(j));
					mDatas.remove(j);
					break;
				}
			}
		}
		updateGroupData();
	}
	
	public String toString() {
		String matchDatas = "";
		for (MahjData data : mMatchDatas) {
			matchDatas = matchDatas + data.getIndex() + ",";
		}
		
		String datas = "";
		for (MahjData data : mDatas) {
			datas = datas + data.getIndex() + ",";
		}
		
		String outDatas = "";
		for (MahjData data : mOutDatas) {
			outDatas = outDatas + data.getIndex() + ",";
		}
		
		int latestData = mLatestData != null ? mLatestData.getIndex() : -1;
		
		String result = "[OUT]" + mIsOuted + ";[OT]" + mOperateType + ";[LD]" + latestData + ";[MD]" + matchDatas
				+ ";[DD]" + datas + ";[OD]" + outDatas;
		
		return result;
	}
}
