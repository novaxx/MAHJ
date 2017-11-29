package nova.common.game.mahjong.util;

import java.util.ArrayList;
import java.util.HashMap;

public class MahjHandlerUtil {

	private static final boolean DEBUG = false;

	public static boolean isHuEnable(final ArrayList<Integer> infos, final int godIndex, final int godCount) {
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
					int needGodCount = getNeedGodCount(godIndex, tmpInfos);
					if (needGodCount <= tmpGodCount) {
						return true;
					} else {
						/** 还原信息 **/
						tmpGodCount = godCount;
						tmpInfos.clear();
						tmpInfos.addAll(infos);
					}
				}
			} else {
				if (is2Combine(tmpInfos.get(i), tmpInfos.get(i + 1))
						&& (i == tmpInfos.size() - 2 || tmpInfos.get(i) != tmpInfos.get(i + 2))) {
					tmpInfos.remove(i + 1);
					tmpInfos.remove(i);
					int needGodCount = getNeedGodCount(godIndex, tmpInfos);
					if (needGodCount <= tmpGodCount) {
						return true;
					} else {
						/** 还原信息 **/
						tmpGodCount = godCount;
						tmpInfos.clear();
						tmpInfos.addAll(infos);
					}
				}

				if (tmpGodCount > 0 && tmpInfos.get(i) != tmpInfos.get(i + 1)) {
					tmpGodCount -= 1;
					tmpInfos.remove(i);
					int needGodCount = getNeedGodCount(godIndex, tmpInfos);
					if (needGodCount <= tmpGodCount) {
						return true;
					} else {
						/** 还原信息 **/
						tmpGodCount = godCount;
						tmpInfos.clear();
						tmpInfos.addAll(infos);
					}
				}
			}
		}

		return false;
	}

	public static int getNeedGodCount(int godIndex, final ArrayList<Integer> infos) {
		if (infos == null || infos.size() <= 0) {
			return 0;
		}

		if (MahjUtil.getMahjColr(infos.get(0)) > 2) {
			return getNeedGodCountByFeng(godIndex, infos);
		}

		return getNeedGodCountByCommon(infos);
	}

	private static int getNeedGodCountByCommon(final ArrayList<Integer> infos) {
		int needGodCount = 0;
		int infoSize = infos.size();
		/** LOG--DEBUG--BEGIN-- **/
		if (DEBUG) {
			System.out.print("getNeedGodCountByCommon--infos.size=" + infoSize + ":" + infos.toString() + "\n");
		}
		/** LOG--DEBUG--END--- **/
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

			// 连续, 不需要王牌
			for (int i = 1; i < infoSize; i++) {
				int v1 = infos.get(i);
				// 普通麻将，差值大于1视为不连续，比如3/1, 17/14, 25/21
				if (v0 - v1 > 1) {
					break;
				}

				if ((i + 2) < infoSize && infos.get(i + 2) == v1) {
					continue;
				}
				// 可以成为一组时，做连续处理
				if ((i + 1) < infoSize && is3Combine(v0, v1, infos.get(i + 1))) {
					infos.remove(i + 1);
					infos.remove(i);
					infos.remove(0);
					/** LOG--DEBUG--BEGIN-- **/
					if (DEBUG) {
						System.out.print("getNeedGodCountByCommon--连续**移除 0, " + i + ", " + (i + 1) + "\n");
					}
					/** LOG--DEBUG--END-- **/
					needGodCount += getNeedGodCountByCommon(infos);
					break;
				}
			}

			// 差一张,需要一张王牌
			infoSize = infos.size();
			for (int i = 1; i < infoSize; i++) {
				int v1 = infos.get(i);
				// 普通麻将，相差超过2时，一张王牌不够，不做处理，比如：19/16， 26/23
				if (v0 - v1 > 2) {
					break;
				}

				if ((i + 1) < infoSize && infos.get(i + 1) == v1) {
					continue;
				}

				infos.remove(i);
				infos.remove(0);
				/** LOG--DEBUG--BEGIN-- **/
				if (DEBUG) {
					System.out.print("getNeedGodCountByCommon--差一个**移除 0, " + i + ", need : " + 1 + "\n");
				}
				/** LOG--DEBUG--END-- **/
				needGodCount += 1;
				needGodCount += getNeedGodCountByCommon(infos);
				break;
			}

			// 只有一张散的，需要两张王牌
			if (infos.size() > 0) {
				infos.remove(0);
				/** LOG--DEBUG--BEGIN-- **/
				if (DEBUG) {
					System.out.print("getNeedGodCountByCommon--只有一个**移除 0, need : 2" + "\n");
				}
				/** LOG--DEBUG--END-- **/
				needGodCount += 2;
				needGodCount += getNeedGodCountByCommon(infos);
			}
		}

		return needGodCount;
	}

	private static int getNeedGodCountByFeng(int godIndex, final ArrayList<Integer> infos) {
		int needGodCount = 0;
		int infoSize = infos.size();
		/** LOG--DEBUG--BEGIN-- **/
		if (DEBUG) {
			System.out.print("getNeedGodCountByFeng--infos.size=" + infoSize + ":" + infos.toString() + "\n");
		}
		/** LOG--DEBUG--END--- **/
		if (infoSize <= 0) {
			/**
			 * 不处理
			 */
		} else if (infoSize == 1) {
			needGodCount += 2;
			infos.removeAll(infos);
		} else if (infoSize == 2) {
			if (infos.get(0) == infos.get(1)) {
				needGodCount += 1;
			} else if (MahjUtil.getMahjColr(infos.get(0)) == MahjUtil.getMahjColr(godIndex)
					&& infos.get(0) != godIndex && infos.get(1) != godIndex) {
				/**
				 * 赖子刚好可以跟麻将配成三个不同的，比如 GOD:西 infos:东,南
				 */
				needGodCount += 1;
			} else {
				needGodCount += 4;
			}
			infos.removeAll(infos);
		} else {
			HashMap<Integer, ArrayList<Integer>> collatedList = getCollatedListByFeng(infos);
			ArrayList<Integer> continuedLists = new ArrayList<Integer>();
			for (int i = 1; i <= 4; i++) {
				if (collatedList.get(i) != null && collatedList.get(i).size() == 2) {
					continuedLists.add(collatedList.get(i).get(0));
				}
			}
			if (continuedLists.size() == 2) {
				for (int face = 1; face <= 4; face++) {
					if (continuedLists.size() >= 3) {
						break;
					}
					if (collatedList.get(face) != null
							&& (collatedList.get(face).size() == 1 || collatedList.get(face).size() == 4)) {
						continuedLists.add(collatedList.get(face).get(0));
						sortList(continuedLists);
					}
				}

				if (continuedLists.size() >= 3) {
					for (int i = 0; i < 3; i++) {
						int index = continuedLists.get(i);
						infos.remove(index);
					}
					needGodCount += getNeedGodCountByFeng(godIndex, infos);
				}
			}

			// 三个不同的成为一组
			collatedList.clear();
			collatedList = getCollatedListByFeng(infos);
			ArrayList<Integer> continuedList = getContinuedListByFeng(collatedList);
			if (continuedList.size() >= 3) {
				for (int i = 0; i < 3; i++) {
					int index = continuedList.get(i);
					infos.remove(index);
				}
				needGodCount += getNeedGodCountByFeng(godIndex, infos);
			}

			// 有三个相同的可以成为一组，四个相同的取三个
			for (int i = 0; i < infos.size() - 2; i++) {
				if (infos.get(i) == infos.get(i + 1) && infos.get(i) == infos.get(i + 2)) {
					infos.remove(i + 2);
					infos.remove(i + 1);
					infos.remove(i);
					needGodCount += getNeedGodCountByFeng(godIndex, infos);
					break;
				}
			}

			collatedList.clear();
			collatedList = getCollatedListByFeng(infos);
			for (int face = 1; face <= 4; face++) {
				if (collatedList.get(face) == null) {
					continue;
				}

				if (collatedList.get(face).size() == 1 || collatedList.get(face).size() == 4) {
					needGodCount += 2;
				} else if (collatedList.get(face).size() == 2) {
					needGodCount += 1;
				}
			}
		}

		return needGodCount;
	}

	/**
	 * 麻将为风时，计算可以成为一组的list 三个不同成为一组
	 */
	private static ArrayList<Integer> getContinuedListByFeng(HashMap<Integer, ArrayList<Integer>> collatedList) {
		ArrayList<Integer> continuedLists = new ArrayList<Integer>();
		/** 先从只有一个的麻将中取，再从四个中取，再从两个中取，最后从三个中取 **/
		int[] orders = { 1, 4, 2, 3 };
		for (int i = 0; i < orders.length; i++) {
			if (continuedLists.size() >= 3) {
				return continuedLists;
			}

			if (i == orders.length - 1 && continuedLists.size() < 2) {
				return continuedLists;
			}

			for (int face = 1; face <= 4; face++) {
				if (continuedLists.size() >= 3) {
					return continuedLists;
				}
				if (collatedList.get(face) != null && collatedList.get(face).size() == orders[i]) {
					continuedLists.add(collatedList.get(face).get(0));
					sortList(continuedLists);
				}
			}
		}

		return continuedLists;
	}

	/**
	 * 从大到小排序
	 */
	private static void sortList(ArrayList<Integer> datas) {
		for (int i = 0; i < datas.size(); i++) {
			// 从第i+1为开始循环数组
			for (int j = i + 1; j < datas.size(); j++) {
				// 如果前一位比后一位小，那么就将两个数字调换
				// 这里是按降序排列
				// 如果你想按升序排列只要改变符号即可
				if (datas.get(j) > datas.get(i)) {
					int tem = datas.get(i);
					datas.set(i, datas.get(j));
					datas.set(j, tem);
				}
			}
		}
	}

	/**
	 * 整理麻将，方便后续计算 key(int) : 麻将face : 1/2/3/4 value(ArrayList<Integer>) :
	 * 麻将对应的index
	 */
	private static HashMap<Integer, ArrayList<Integer>> getCollatedListByFeng(ArrayList<Integer> infos) {
		HashMap<Integer, ArrayList<Integer>> collatedList = new HashMap<Integer, ArrayList<Integer>>();
		for (int i = 0; i < infos.size(); i++) {
			int face = MahjUtil.getMahjFace(infos.get(i));
			if (collatedList.get(face) == null) {
				collatedList.put(face, new ArrayList<Integer>());
			}
			collatedList.get(face).add(i);
		}

		return collatedList;
	}

	/**
	 * 
	 * @param mj1
	 * @param mj2
	 * @param mj3
	 * @return 是否可以组成一组 1. 三个相同为一组 2. 非风，连续为一组 3. 风，三个不同为一组(洪洞麻将)
	 */
	private static boolean is3Combine(int mj1, int mj2, int mj3) {
		if (mj1 == mj2 && mj1 == mj3) {
			return true;
		}

		boolean isFeng = MahjUtil.getMahjColr(mj1) > 2;
		if (!isFeng && (mj1 - 1) == mj2 && (mj1 - 2) == mj3) {
			return true;
		}

		if (isFeng && mj1 != mj2 && mj1 != mj3 && mj2 != mj3) {
			return true;
		}

		return false;
	}

	private static boolean is2Combine(int mj1, int mj2) {
		return mj1 == mj2;
	}
}
