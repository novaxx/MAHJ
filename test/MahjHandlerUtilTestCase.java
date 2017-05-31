package test;

import junit.framework.TestCase;
import nova.common.game.mahjong.util.MahjHandlerUtil;

public class MahjHandlerUtilTestCase extends TestCase {
	
	private static final String[][] TEST_NEEDGOD_DATA = {
			{"0", ""},
			{"2",  "2"},
			{"1", "1,1"},
			{"1", "2,1"},
			{"1", "15,15,13,12,11"},
			{"0", "15,15,15,13,12,11"},
			{"1", "16,15,15,14,13"},
			{"2", "15,15,14,12"},
			{"4", "29,27,27,26,25,24,24,24,22,21,21"},
			{"4", "27,27,27,27,26,24,22,21"},
			{"2", "29,29,29,29,28,28,27,27,27,26,26,24,22"},
			{"2", "42"},
			{"4", "43,42"},
			{"0", "43,42,41"},
			{"0", "34,32,31"},
			{"1", "34,33,33,32,31"},
			{"0", "34,33,33,32,32,31"},
			{"0", "34,33,33,33,32,31"},
	};
	
	private static final String[][] TEST_NEEDGOD_FENG_DATA = {
			{"0", ""},
			{"2", "42"},
			{"4", "43,42"},
			{"0", "43,42,41"},
			{"1", "43,43,43,42,41"},
			{"2", "43,43,42,42,42,42,41"},
			{"0", "34,32,31"},
			{"1", "34,33,33,32,31"},
			{"0", "34,33,33,32,32,31"},
			{"0", "34,33,33,33,32,31"},
			{"1", "34,34,34,33,33,32,32,31"},
			{"1", "34,34,34,34,33,33,32,32,32,32,31"},
	};
	
	private static final String[][] TEST_HUJIANG_FENG_DATA = {
			{"0", "", "false"},
			{"1", "", "false"},
			{"2", "", "true"},
			{"1", "42", "true"},
			{"3", "43,42", "true"},
			{"1", "43,42,41", "false"},
			{"0", "43,43,43,42,41", "true"},
			{"1", "43,43,42,42,42,42,41", "true"},
			{"1", "34,32,31", "false"},
			{"0", "34,33,33,32,31", "true"},
			{"1", "34,33,33,32,32,31", "false"},
			{"2", "34,33,33,33,32,31", "true"},
			{"0", "34,34,34,33,33,32,32,31", "true"},
			{"0", "34,34,34,34,33,33,32,32,32,32,31", "true"},
	};
	
	private static final String[][] TEST_HUJIANG_DATA = {
			{"0", "", "false"},
			{"1", "", "false"},
			{"2", "", "true"},
			{"1", "15,14,13,12", "true"},
			{"0", "15,15,14,13,12", "true"},
			{"3", "16,15,15,14,13", "true"},
			{"1", "16,15,15,14,13", "false"},
			{"3", "27,27,27,27,26,24,22,21", "true"},
			{"2", "27,27,27,27,26,24,22,21", "false"},
			{"3", "29,27,27,26,25,24,24,24,22,21,21", "true"},
			{"2", "29,27,27,26,25,24,24,24,22,21,21", "false"},
			{"4", "29,29,29,29,28,28,27,27,27,26,26,24,22", "true"},
			{"3", "29,29,29,29,28,28,27,27,27,26,26,24,22", "false"},
			{"0", "19,19,19,18,17,16,15,14,13,12,11,11,11,11", "true"},
			{"0", "19,19,19,18,17,16,15,14,14,13,12,11,11,11", "true"},
	};
	
	public void test_getGodCount_Not_Jiang() {
		for (int i = 0; i < TEST_NEEDGOD_DATA.length; i++) {
			int needGodCount = MahjHandlerUtil.getNeedGodCount(TestUtil.getMahjIndexs(TEST_NEEDGOD_DATA[i][1]));
			assertEquals((int) Integer.valueOf(TEST_NEEDGOD_DATA[i][0]), needGodCount);
		}
	}

	public void test_getGodCount_Feng_Not_Jiang() {
		for (int i = 0; i < TEST_NEEDGOD_FENG_DATA.length; i++) {
			int needGodCount = MahjHandlerUtil.getNeedGodCount(TestUtil.getMahjIndexs(TEST_NEEDGOD_FENG_DATA[i][1]));
			assertEquals((int) Integer.valueOf(TEST_NEEDGOD_FENG_DATA[i][0]), needGodCount);
		}
	}

	public void test_isHuEnable_Jiang() {
		for (int i = 0; i < TEST_HUJIANG_DATA.length; i++) {
			String data = TEST_HUJIANG_DATA[i][1];
			int godCount = Integer.valueOf(TEST_HUJIANG_DATA[i][0]);
			boolean expected = Boolean.valueOf(TEST_HUJIANG_DATA[i][2]);
			boolean isHu = MahjHandlerUtil.isHuEnable(TestUtil.getMahjIndexs(data), godCount);
			assertEquals(expected, isHu);
		}
	}

	public void test_isHuEnable_FENG_Jiang() {
		for (int i = 0; i < TEST_HUJIANG_FENG_DATA.length; i++) {
			String data = TEST_HUJIANG_FENG_DATA[i][1];
			int godCount = Integer.valueOf(TEST_HUJIANG_FENG_DATA[i][0]);
			boolean expected = Boolean.valueOf(TEST_HUJIANG_FENG_DATA[i][2]);
			boolean isHu = MahjHandlerUtil.isHuEnable(TestUtil.getMahjIndexs(data), godCount);
			assertEquals(expected, isHu);
		}
	}
}
