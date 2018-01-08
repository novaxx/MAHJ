package nova.common.game.mahjong.util;

import java.util.ArrayList;
import java.util.HashMap;

import nova.common.game.mahjong.data.FanData;
import nova.common.game.mahjong.data.MahjData;
import nova.common.game.mahjong.data.MahjGroupData;
import nova.common.game.mahjong.data.MahjResultData;
import nova.common.game.mahjong.util.MahjGameCommand.FanType;

@SuppressWarnings("unused")
public class MahjResultUtil {

	public static HashMap<Integer, MahjResultData> getResultForGroupDatas(final HashMap<Integer, MahjGroupData> groupDatas, final int winner) {
		HashMap<Integer, MahjResultData> results = new HashMap<Integer, MahjResultData>();
		for (int i = 0; i < 4; i++) {
			if (winner == i) {
				results.put(i, getResultForWinner(groupDatas.get(i)));
			} else {
				results.put(i, getResultForLoser(groupDatas.get(i)));
			}
		}
		
		updateBalanceForResults(results);
		
		return results;
	}
	
	private static void updateBalanceForResults(HashMap<Integer, MahjResultData> results) {
		int totalFan = 0;
		for (int i = 0; i < 4; i++) {
			totalFan += results.get(i).getTotalFan();
		}
		
		for (int i = 0; i < 4; i++) {
			int balance = (4 * results.get(i).getTotalFan() - totalFan) * 100;
			results.get(i).setBalance(balance);
		}
	}
	
	public static MahjResultData getResultForLoser(MahjGroupData groupData) {
		MahjResultData result = new MahjResultData();
		int index = -1;
		int count = 0;
		int fan = 0;
		for (MahjData data : groupData.getMatchDatas()) {
			if (index == -1) {
				index = data.getIndex();
				count++;
			} else if (index != data.getIndex()) {
				index = data.getIndex();
				if (count >= 4) {
					fan++;
				}
				count = 1;
			} else {
				count++;
			}
		}
		
		if (count >= 4) {
			fan++;
		}
		
		result.setTotalFan(fan);
		return result;
	}
	
	public static MahjResultData getResultForWinner(MahjGroupData groupData) {
		MahjResultData result = new MahjResultData();
		ArrayList<ArrayList<String>> huList = groupData.getHuList();
		result.setOrders(huList.get(0));
		if (isCheckSiFanDaFa(groupData)) {
			result.setTotalFan(168);
			result.addFanData(new FanData(FanType.TYPE_SIFANDAFA, 168));
			return result;
		}

		if (isCheckTianHu(groupData)) {
			result.setTotalFan(168);
			result.addFanData(new FanData(FanType.TYPE_TIANHU, 168));
			return result;
		}

		if (isCheckDaSiXi(groupData)) {
			result.setTotalFan(88);
			result.addFanData(new FanData(FanType.TYPE_DASIXI, 88));
			return result;
		}
		
		if (isCheckDaSanYuan(groupData)) {
			result.setTotalFan(88);
			result.addFanData(new FanData(FanType.TYPE_DASANYUAN, 88));
			return result;
		}
		
		if (isCheckLianQiDui(groupData)) {
			result.setTotalFan(88);
			result.addFanData(new FanData(FanType.TYPE_LIANQIDUI, 88));
			return result;
		}
		
		if (isCheckSiGanZi(groupData)) {
			result.setTotalFan(88);
			result.addFanData(new FanData(FanType.TYPE_SIGAN, 88));
			return result;
		}
		
		if (isCheckQiDui(groupData)) {
			result.setTotalFan(24);
			result.addFanData(new FanData(FanType.TYPE_QIDUI, 24));
			return result;
		}
		
		int fan = 1;
		result.addFanData(new FanData(FanType.TYPE_HU, 1));
		
		if (isCheckQingYiSe(groupData)) {
			fan += 10;
			result.addFanData(new FanData(FanType.TYPE_QINGYISE, 10));
		}
		
		if (isCheckMenQianQing(groupData)) {
			fan += 2;
			result.addFanData(new FanData(FanType.TYPE_MENQIANQIN, 2));
		}
		
		int gangCount = checkGangCount(groupData);
		if (gangCount > 0) {
			fan += gangCount;
			result.addFanData(new FanData(FanType.TYPE_GANG, gangCount));
		}
		
		int maxFan = 0;
		int maxIndex = 0;
		for (int i = 0; i < huList.size(); i++) {
			int tmpFan = 0;
			int fengCount = checkFengCount(huList.get(i));
			if (fengCount > 0) {
				tmpFan += fengCount;
			}
			if (isCheckDiaoJian(huList.get(i), groupData)) {
				tmpFan += 1;
			}
			
			if (tmpFan > maxFan) {
				maxFan = tmpFan;
				maxIndex = i;
			}
		}
		
		if (maxFan > 0) {
			fan += maxFan;
			result.setOrders(huList.get(maxIndex));
			int fengCount = checkFengCount(huList.get(maxIndex));
			if (fengCount > 0) {
				result.addFanData(new FanData(FanType.TYPE_FENG, fengCount));
			}
			if (isCheckDiaoJian(huList.get(maxIndex), groupData)) {
				result.addFanData(new FanData(FanType.TYPE_DIAOJIAN, 1));
			}
		}
		
		result.setTotalFan(fan);
		return result;
	}

	/*
	 * 「四方大发」 
	 * 东东东东西西西西南南南南北北北北发发 
	 * 东东东西西西南南南北北北发发
	 */
	private static boolean isCheckSiFanDaFa(MahjGroupData groupData) {
		HashMap<Integer, ArrayList<Integer>> results = getDataResult(groupData);
		if (results.get(2) != null && results.get(2).size() == 1 && results.get(2).get(0) == 42) {
			if (results.get(1) == null && results.get(3) == null 
					&& results.get(4) != null && results.get(4).size() == 4 && getCommonColor(results.get(4)) == 3) {
				return true;
			}
			
			if (results.get(1) == null && results.get(4) == null
					&& results.get(3) != null && results.get(3).size() == 4 && getCommonColor(results.get(3)) == 3) {
				return true;
			}
		}
		return false;
	}

	/*
	 * 「天和」起牌即和
	 */
	private static boolean isCheckTianHu(MahjGroupData groupData) {
		if (!groupData.isOuted()) {
			return true;
		}
		return false;
	}

	private static boolean isCheck13Yao(MahjGroupData groupData) {
		return false;
	}

	private static boolean isCheckDaSiXi(MahjGroupData groupData) {
		HashMap<Integer, ArrayList<Integer>> results = getDataResult(groupData);
		if (results.get(4) != null && results.get(4).contains(31) 
				&& results.get(4).contains(32) && results.get(4).contains(33) && results.get(4).contains(34)) {
			return true;
		}

		if (results.get(3) != null && results.get(3).contains(31) 
				&& results.get(3).contains(32) && results.get(3).contains(33) && results.get(3).contains(34)) {
			return true;
		}
		return false;
	}

	private static boolean isCheckDaSanYuan(MahjGroupData groupData) {
		HashMap<Integer, ArrayList<Integer>> results = getDataResult(groupData);
		if (results.get(4) != null && results.get(4).contains(41) 
				&& results.get(4).contains(42) && results.get(4).contains(43)) {
			return true;
		}

		if (results.get(3) != null && results.get(3).contains(41) 
				&& results.get(3).contains(42) && results.get(3).contains(43)) {
			return true;
		}
		return false;
	}

	private static boolean isCheckJiuLianDeng(MahjGroupData groupData) {
		return false;
	}

	private static boolean isCheckLianQiDui(MahjGroupData groupData) {
		HashMap<Integer, ArrayList<Integer>> results = getDataResult(groupData);
		if (results.get(1) != null && results.get(3) != null && results.get(4) != null) {
			return false;
		}
		
		if (results.get(2) != null && results.get(2).size() == 7) {
			int index = 0;
			for (int i = 0; i < results.get(2).size(); i++) {
				if (i > 0 && results.get(2).get(i) - index != 1) {
					return false;
				}
				index = results.get(2).get(i);
			}
			
			return true;
		}
		
		
		return false;
	}

	private static boolean isCheckLvYiSe(MahjGroupData groupData) {
		return false;
	}

	private static boolean isCheckSiGanZi(MahjGroupData groupData) {
		HashMap<Integer, ArrayList<Integer>> results = getDataResult(groupData.getMatchDatas());
		if (results.get(4) != null && results.get(4).size() > 3) {
			return true;
		}
		return false;
	}

	private static boolean isCheckHunGan(MahjGroupData groupData) {
		return false;
	}

	private static boolean isCheckZiYiSe(MahjGroupData groupData) {
		return false;
	}

	private static boolean isCheckMenQianQing(MahjGroupData groupData) {
		if (groupData.getMatchDatas() == null || groupData.getMatchDatas().size() <= 0) {
			return true;
		}
		return false;
	}

	private static boolean isCheckQiDui(MahjGroupData groupData) {
		HashMap<Integer, ArrayList<Integer>> results = getDataResult(groupData);
		if (results.get(1) != null && results.get(3) != null && results.get(4) != null) {
			return false;
		}
		if (results.get(2) != null && results.get(2).size() == 7) {
			return true;
		}
		
		return false;
	}

	private static boolean isCheckQingYiSe(MahjGroupData groupData) {
		int color = -1;
		int god = groupData.getGodIndex();
		if (groupData.getMatchDatas() != null) {
			for (MahjData data : groupData.getMatchDatas()) {
				if (data.getIndex() != god && MahjUtil.getMahjColr(data.getIndex()) <= 2) {
					if (color == -1) {
						color = MahjUtil.getMahjColr(data.getIndex());
					} else if (MahjUtil.getMahjColr(data.getIndex()) != color) {
						return false;
					}
				}
			}
		}

		if (groupData.getDatas() != null) {
			for (MahjData data : groupData.getDatas()) {
				if (data.getIndex() != god && MahjUtil.getMahjColr(data.getIndex()) <= 2) {
					if (color == -1) {
						color = MahjUtil.getMahjColr(data.getIndex());
					} else if (MahjUtil.getMahjColr(data.getIndex()) != color) {
						return false;
					}
				}
			}
		}

		if (groupData.getLatestData().getIndex() != god
				&& MahjUtil.getMahjColr(groupData.getLatestData().getIndex()) <= 2) {
			if (color == -1) {
				color = MahjUtil.getMahjColr(groupData.getLatestData().getIndex());
			} else if (MahjUtil.getMahjColr(groupData.getLatestData().getIndex()) != color) {
				return false;
			}
		}

		if (color >= 0) {
			return true;
		} else {
			return false;
		}
	}

	private static int checkFengCount(ArrayList<String> orders) {
		int fengCount = 0;
		for (String order : orders) {
			String[] indexs = order.split(",");
			if (indexs.length != 3 || MahjUtil.getMahjColr(Integer.valueOf(indexs[0])) < 3) {
				continue;
			}
			if (Integer.valueOf(indexs[0]) != Integer.valueOf(indexs[1])
					&& Integer.valueOf(indexs[0]) != Integer.valueOf(indexs[2])
					&& Integer.valueOf(indexs[1]) != Integer.valueOf(indexs[2])) {
				fengCount++;
			}
		}
		return fengCount;
	}

	private static int checkGangCount(MahjGroupData groupData) {
		HashMap<Integer, ArrayList<Integer>> results = getDataResult(groupData.getMatchDatas());
		if (results.get(4) != null) {
			return results.get(4).size();
		}
		
		return 0;
	}

	private static boolean isCheckDiaoJian(ArrayList<String> orders, MahjGroupData groupData) {
		ArrayList<Integer> jiang = new ArrayList<Integer>();
		for (String order : orders) {
			String[] indexs = order.split(",");
			if (indexs.length == 2) {
				for (String index : indexs) {
					if (index.equals("G")) {
						jiang.add(groupData.getGodIndex());
					} else {
						jiang.add(Integer.valueOf(index));
					}
				}
				break;
			}
		}
		
		if (jiang.size() == 2 && jiang.contains(groupData.getLatestData().getIndex())) {
			return true;
		}
		
		return false;
	}

	private static int getCommonColor(ArrayList<Integer> indexs) {
		int color = indexs.get(0) / 10;
		for (Integer index : indexs) {
			if (color != index / 10) {
				return -1;
			}
		}

		return color;
	}

	private static HashMap<Integer, ArrayList<Integer>> getDataResult(MahjGroupData groupData) {
		ArrayList<MahjData> datas = new ArrayList<MahjData>();
		datas.addAll(groupData.getMatchDatas());
		datas.addAll(groupData.getDatas());
		datas.add(groupData.getLatestData());
		return getDataResult(datas);
	}
	
	private static HashMap<Integer, ArrayList<Integer>> getDataResult(ArrayList<MahjData> datas) {
		HashMap<Integer, ArrayList<Integer>> results = new HashMap<Integer, ArrayList<Integer>>();
		sortData(datas);
		int index = 0;
		int count = 0;
		for (MahjData data : datas) {
			if (data.getIndex() == index) {
				count++;
			} else {
				if (index > 0 && count > 0) {
					if (results.get(count) == null) {
						results.put(count, new ArrayList<Integer>());
					}
					results.get(count).add(index);
				}
				index = data.getIndex();
				count = 1;
			}
		}

		if (results.get(count) == null) {
			results.put(count, new ArrayList<Integer>());
		}
		results.get(count).add(index);

		return results;
	}

	private static void sortData(ArrayList<MahjData> datas) {
		for (int i = 0; i < datas.size(); i++) {
			// 从第i+1为开始循环数组
			for (int j = i + 1; j < datas.size(); j++) {
				// 如果后一位比前一位小，那么就将两个数字调换
				// 这里是按升序排列
				// 如果你想按降序排列只要改变符号即可
				if (datas.get(j).getIndex() < datas.get(i).getIndex()) {
					MahjData tem = datas.get(i);
					datas.set(i, datas.get(j));
					datas.set(j, tem);
				}
			}
		}
	}
}
