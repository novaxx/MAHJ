package test;

import java.util.ArrayList;

import nova.common.game.mahjong.data.MahjData;

public class TestUtil {
	public static ArrayList<MahjData> getMahjDatas(String data) {
		ArrayList<MahjData> mMahjDatas = new ArrayList<MahjData>();
		String datas[] = data.split(",");
		for (int i = 0; i < datas.length; i++) {
			mMahjDatas.add(new MahjData(Integer.valueOf(datas[i])));
		}

		return mMahjDatas;
	}
}
