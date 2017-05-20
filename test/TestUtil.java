package test;

import java.util.ArrayList;

import nova.common.game.mahjong.data.MahjData;

public class TestUtil {
	public static ArrayList<MahjData> getMahjDatas(String data) {
		ArrayList<MahjData> mahjDatas = new ArrayList<MahjData>();
		String datas[] = data.split(",");
		for (int i = 0; i < datas.length; i++) {
			if (datas[i].isEmpty()) {
				continue;
			}
			mahjDatas.add(new MahjData(Integer.valueOf(datas[i])));
		}

		return mahjDatas;
	}
	
	public static ArrayList<Integer> getMahjIndexs(String data) {
		ArrayList<Integer> mahjIndexs = new ArrayList<Integer>();
		String datas[] = data.split(",");
		for (int i = 0; i < datas.length; i++) {
			if (datas[i].isEmpty()) {
				continue;
			}
			mahjIndexs.add(Integer.valueOf(datas[i]));
		}

		return mahjIndexs;
	}
}
