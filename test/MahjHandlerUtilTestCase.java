package test;

import junit.framework.TestCase;
import nova.common.game.mahjong.util.MahjHandlerUtil;

public class MahjHandlerUtilTestCase extends TestCase {
	
	private static final String[][] TEST_NEEDGOD_DATA = {
			/**{"god", "godcount", "datas"}**/
			{"29", "0", ""},
			{"29", "2",  "2"},
			{"29", "1", "1,1"},
			{"29", "1", "2,1"},
			{"29", "1", "15,15,13,12,11"},
			{"29", "0", "15,15,15,13,12,11"},
			{"29", "1", "16,15,15,14,13"},
			{"29", "2", "15,15,14,12"},
			{"19", "4", "29,27,27,26,25,24,24,24,22,21,21"},
			{"19", "4", "27,27,27,27,26,24,22,21"},
			{"19", "2", "29,29,29,29,28,28,27,27,27,26,26,24,22"},
			{"29", "2", "42"},
			{"29", "4", "43,42"},
			{"29", "0", "43,42,41"},
			{"29", "0", "34,32,31"},
			{"29", "1", "34,33,33,32,31"},
			{"29", "0", "34,33,33,32,32,31"},
			{"29", "0", "34,33,33,33,32,31"},
	};
	
	private static final String[][] TEST_NEEDGOD_FENG_DATA = {
			/**{"god", "godcount", "datas"}**/
			{"16", "0", ""},
			{"16", "2", "42"},
			{"16", "4", "43,42"},
			{"41", "1", "43,42"},
			{"16", "0", "43,42,41"},
			{"16", "1", "43,43,43,42,41"},
			{"16", "2", "43,43,42,42,42,42,41"},
			{"16", "0", "34,32,31"},
			{"16", "1", "34,33,33,32,31"},
			{"16", "0", "34,33,33,32,32,31"},
			{"16", "0", "34,33,33,33,32,31"},
			{"16", "1", "34,34,34,33,33,32,32,31"},
			{"16", "1", "34,34,34,34,33,33,32,32,32,32,31"},
			{"32", "1", "34,33"},
			{"32", "4", "34,32"},
			{"32", "2", "34,34,33,32"},
	};
	
	private static final String[][] TEST_HUJIANG_FENG_DATA = {
			/**{"god", "godcount", "datas", "expected"}**/
			{"15", "0", "", "false"},
			{"15", "1", "", "false"},
			{"15", "2", "", "true"},
			{"15", "1", "42", "true"},
			{"15", "3", "43,42", "true"},
			{"15", "1", "43,42,41", "false"},
			{"42", "1", "43,42,42,41", "true"},
			{"15", "0", "43,43,43,42,41", "true"},
			{"15", "1", "43,43,42,42,42,42,41", "true"},
			{"15", "1", "34,32,31", "false"},
			{"15", "0", "34,33,33,32,31", "true"},
			{"15", "1", "34,33,33,32,32,31", "false"},
			{"31", "1", "34,33,33,32", "true"},
			{"15", "2", "34,33,33,33,32,31", "true"},
			{"15", "0", "34,34,34,33,33,32,32,31", "true"},
			{"15", "0", "34,34,34,34,33,33,32,32,32,32,31", "true"},
	};
	
	private static final String[][] TEST_HUJIANG_DATA = {
			/**{"god", "godcount", "datas", "expected"}**/
			{"33", "0", "", "false"},
			{"33", "1", "", "false"},
			{"33", "2", "", "true"},
			{"33", "1", "15,14,13,12", "true"},
			{"33", "0", "15,15,14,13,12", "true"},
			{"33", "3", "16,15,15,14,13", "true"},
			{"33", "1", "16,15,15,14,13", "false"},
			{"33", "3", "27,27,27,27,26,24,22,21", "true"},
			{"33", "2", "27,27,27,27,26,24,22,21", "false"},
			{"33", "3", "29,27,27,26,25,24,24,24,22,21,21", "true"},
			{"33", "2", "29,27,27,26,25,24,24,24,22,21,21", "false"},
			{"33", "4", "29,29,29,29,28,28,27,27,27,26,26,24,22", "true"},
			{"33", "3", "29,29,29,29,28,28,27,27,27,26,26,24,22", "false"},
			{"33", "0", "19,19,19,18,17,16,15,14,13,12,11,11,11,11", "true"},
			{"33", "0", "19,19,19,18,17,16,15,14,14,13,12,11,11,11", "true"},
	};
	
	public void test_getGodCount_Not_Jiang() {
		for (int i = 0; i < TEST_NEEDGOD_DATA.length; i++) {
			int needGodCount = MahjHandlerUtil.getNeedGodCount(Integer.valueOf(TEST_NEEDGOD_DATA[i][0]), TestUtil.getMahjIndexs(TEST_NEEDGOD_DATA[i][2]), null);
			assertEquals((int) Integer.valueOf(TEST_NEEDGOD_DATA[i][1]), needGodCount);
		}
	}

	public void test_getGodCount_Feng_Not_Jiang() {
		for (int i = 0; i < TEST_NEEDGOD_FENG_DATA.length; i++) {
			int needGodCount = MahjHandlerUtil.getNeedGodCount(Integer.valueOf(TEST_NEEDGOD_FENG_DATA[i][0]), TestUtil.getMahjIndexs(TEST_NEEDGOD_FENG_DATA[i][2]), null);
			assertEquals((int) Integer.valueOf(TEST_NEEDGOD_FENG_DATA[i][1]), needGodCount);
		}
	}

	public void test_isHuEnable_Jiang() {
		for (int i = 0; i < TEST_HUJIANG_DATA.length; i++) {
			String data = TEST_HUJIANG_DATA[i][2];
			int godCount = Integer.valueOf(TEST_HUJIANG_DATA[i][1]);
			boolean expected = Boolean.valueOf(TEST_HUJIANG_DATA[i][3]);
			boolean isHu = MahjHandlerUtil.isHuEnable(TestUtil.getMahjIndexs(data), Integer.valueOf(TEST_HUJIANG_DATA[i][0]), godCount, null);
			assertEquals(expected, isHu);
		}
	}

	public void test_isHuEnable_FENG_Jiang() {
		for (int i = 0; i < TEST_HUJIANG_FENG_DATA.length; i++) {
			String data = TEST_HUJIANG_FENG_DATA[i][2];
			int godCount = Integer.valueOf(TEST_HUJIANG_FENG_DATA[i][1]);
			boolean expected = Boolean.valueOf(TEST_HUJIANG_FENG_DATA[i][3]);
			boolean isHu = MahjHandlerUtil.isHuEnable(TestUtil.getMahjIndexs(data), Integer.valueOf(TEST_HUJIANG_FENG_DATA[i][0]), godCount, null);
			assertEquals(expected, isHu);
		}
	}
}
