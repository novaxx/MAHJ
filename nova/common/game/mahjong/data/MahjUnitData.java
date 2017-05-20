package nova.common.game.mahjong.data;

import java.util.ArrayList;
import java.util.HashMap;

public class MahjUnitData {

	private ArrayList<MahjData> mDatas = new ArrayList<MahjData>();
	private HashMap<Integer, ArrayList<Integer>> mUnitInfos = new HashMap<Integer, ArrayList<Integer>>();
	private int mGrade;
	private static final boolean DEBUG = false;
	
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
	
	private boolean isShunZiEnable(ArrayList<Integer> infos, int godCount) {
		int size = 0;
		if (infos.size() <= 0) {
			return true;
		}
		
		if (infos.size() == 1) {
			size = 1;
		} else if (infos.size() == 2) {
			if (infos.get(0) - infos.get(1) <= 2) {
				size = 2;
			} else {
				size = 1;
			}
		} else {
			if (infos.get(0) - infos.get(1) == 1) {
				if (infos.get(1) - infos.get(2) == 1) {
					size = 3;
				} else {
					size = 2;
				}
			} else if (infos.get(0) - infos.get(1) == 2) {
				size = 2;
			} else {
				size = 1;
			}
		}
		
		int needGodCount = 3 - size;
		if (godCount >= needGodCount) {
			godCount = godCount - needGodCount;
			for (int i = size - 1; i >= 0; i--) {
				infos.remove(i);
			}
			return true;
		}
		
		return false;
	}
	
	public int getNeedGodCount(ArrayList<Integer> infos, int currentGodCount) {
		int needGodCount = 0;
		int infoSize = infos.size();
		/**LOG--DEBUG--BEGIN--**/
		if (DEBUG) {
			System.out.print("getGodCountForShunZi--infos.size=" + infoSize + ":" + infos.toString() + "\n");
		}
		/**LOG--DEBUG--END---**/
		if (infoSize <= 0) {
			/**
			 * 不处理
			 */
		} else if (infoSize == 1) {
			needGodCount += 2;
			infos.removeAll(infos);
		} else if (infoSize == 2) {
			if (infos.get(0) - infos.get(1) <= 2) {
				needGodCount += 1;
			} else {
				needGodCount += 4;
			}
			infos.removeAll(infos);
		} else {
			int v0 = infos.get(0);
			infoSize = infos.size();
			
			// 连续
			for (int i = 1; i < infoSize; i++) {
				int v1 = infos.get(i);
				if (v0 - v1 > 1) {
					break;
				}
				
				if ((i + 2) < infoSize && infos.get(i + 2) == v1) {
					continue;
				}
				
				if ((i + 1) < infoSize && is3Combine(v0, v1, infos.get(i + 1))) {
					infos.remove(i + 1);
					infos.remove(i);
					infos.remove(0);
					/**LOG--DEBUG--BEGIN--**/
					if (DEBUG) {
						System.out.print("getGodCountForShunZi--连续**移除 0, " +  i + ", " + (i + 1) + "\n");
					}
					/**LOG--DEBUG--END--**/
					needGodCount += getNeedGodCount(infos, needGodCount);
					break;
				}
			}
			
			// 差一个
			infoSize = infos.size();
			for (int i = 1; i < infoSize; i++) {
				int v1 = infos.get(i);
				if (v0 - v1 > 2) {
					break;
				}
				
				if ((i + 1) < infoSize && infos.get(i + 1) == v1) {
					continue;
				}
				
				infos.remove(i);
				infos.remove(0);
				/**LOG--DEBUG--BEGIN--**/
				if (DEBUG) {
					System.out.print("getGodCountForShunZi--差一个**移除 0, " +  i + ", need : " + 1 + "\n");
				}
				/**LOG--DEBUG--END--**/
				needGodCount += 1;
				needGodCount += getNeedGodCount(infos, needGodCount);
				break;
			}
			
			// 只有一个
			if (infos.size() > 0) {
				infos.remove(0);
				/**LOG--DEBUG--BEGIN--**/
				if (DEBUG) {
					System.out.print("getGodCountForShunZi--只有一个**移除 0, need : 2" + "\n");
				}
				/**LOG--DEBUG--END--**/
				needGodCount += 2;
				needGodCount += getNeedGodCount(infos, needGodCount);
			}
		}
		
		return needGodCount;
	}
	
	public boolean isHuEnable(final ArrayList<Integer> infos, final int godCount) {
		int tmpGodCount = godCount;
		ArrayList<Integer> tmpInfos = new ArrayList<Integer>();
		tmpInfos.addAll(infos);
		if (tmpInfos.size() <= 0) {
			return godCount >= 2 ? true : false;
		}
		
		for (int i = 0; i < tmpInfos.size(); i++) {
			if (tmpInfos.size() - 1 == i) {
				if (tmpGodCount > 0) {
					tmpGodCount -= 1;
					tmpInfos.remove(i);
					int needGodCount = getNeedGodCount(tmpInfos, tmpGodCount);
					if (needGodCount <= tmpGodCount) {
						return true;
					} else {
						/**还原信息**/
						tmpGodCount = godCount;
						tmpInfos.clear();
						tmpInfos.addAll(infos);
					}
				}
			} else {
					if (is2Combine(tmpInfos.get(i), tmpInfos.get(i + 1)) && 
							(i == tmpInfos.size() - 2 || tmpInfos.get(i) != tmpInfos.get(i + 2))) {
						tmpInfos.remove(i + 1);
						tmpInfos.remove(i);
						int needGodCount = getNeedGodCount(tmpInfos, tmpGodCount);
						if (needGodCount <= tmpGodCount) {
							return true;
						} else {
							/**还原信息**/
							tmpGodCount = godCount;
							tmpInfos.clear();
							tmpInfos.addAll(infos);
						}
					}
					
					if (tmpGodCount > 0 && tmpInfos.get(i) != tmpInfos.get(i + 1)) {
						tmpGodCount -= 1;
						tmpInfos.remove(i);
						int needGodCount = getNeedGodCount(tmpInfos, tmpGodCount);
						if (needGodCount <= tmpGodCount) {
							return true;
						} else {
							/**还原信息**/
							tmpGodCount = godCount;
							tmpInfos.clear();
							tmpInfos.addAll(infos);
						}
					}
				}
			}
		
		return false;
	}
	
	private boolean is3Combine(int mj1, int mj2, int mj3) {
		if (mj1 == mj2 && mj1 == mj3) {
			return true;
		}
		
		if ((mj1 - 1) == mj2 && (mj1 - 2) == mj3) {
			return true;
		}
		return false;
	}
	
	private boolean is2Combine(int mj1, int mj2) {
		return mj1 == mj2;
	}
}