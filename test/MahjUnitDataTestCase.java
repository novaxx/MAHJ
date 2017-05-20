package test;

import junit.framework.TestCase;
import nova.common.game.mahjong.data.MahjData;
import nova.common.game.mahjong.data.MahjUnitData;

public class MahjUnitDataTestCase extends TestCase {
	private static final String[] TEST_DATA = {
			"12,12,12,11,13,13,15",
			"33,33,33,32,31,31"
	};
	
	private static final String[][] TEST_MATCH_DATA = {
			{"0", "", "13"},
			{"110", "12,12,12,11,13,13,15", "12"},
			{"10", "12,12,12,11,13,13,15", "13"},
			{"110", "33,33,33,32,31,31", "33"},
			{"10", "33,33,33,32,31,31", "31"}
	};
	
	private static final String[][] TEST_GRADE_DATA = {
			{"0", ""},
			{"1", "42"},
			{"5", "12,13,15,16,18"},
			{"5", "1,2,3,5,7"},
			{"4", "13,13,13,11"},
			{"4", "22,22,22,22"},
	};
	
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
	};
	
	private MahjUnitData mUnitData;
	
	public void test_add() {
		mUnitData = new MahjUnitData();
		mUnitData.add(new MahjData(14));
		assertEquals(1, mUnitData.size());
	}
	
	public void test_addAll() {
		mUnitData = new MahjUnitData();
		mUnitData.addAll(TestUtil.getMahjDatas(TEST_DATA[0]));
		assertEquals(7, mUnitData.size());
	}
	
	public void test_matchType() {
		for (int i = 0; i < TEST_MATCH_DATA.length; i++) {
			mUnitData = new MahjUnitData();
			mUnitData.addAll(TestUtil.getMahjDatas(TEST_MATCH_DATA[i][1]));
			mUnitData.updateUnitDataInfo();
			assertEquals((int)Integer.valueOf(TEST_MATCH_DATA[i][0]), 
					mUnitData.getMatchType(new MahjData(Integer.valueOf(TEST_MATCH_DATA[i][2]))));
		}
	}
	
	public void test_getGrade() {
		for (int i = 0; i < TEST_GRADE_DATA.length; i++) {
			mUnitData = new MahjUnitData();
			mUnitData.addAll(TestUtil.getMahjDatas(TEST_GRADE_DATA[i][1]));
			mUnitData.updateUnitDataInfo();
			assertEquals((int)Integer.valueOf(TEST_GRADE_DATA[i][0]), mUnitData.grade());
		}
	}
	
	public void test_getGodCount_Not_Jiang() {
		for (int i = 0; i < TEST_NEEDGOD_DATA.length; i++) {
			mUnitData = new MahjUnitData();
			long start_time = System.currentTimeMillis();
			int needGodCount = mUnitData.getNeedGodCount(TestUtil.getMahjIndexs(TEST_NEEDGOD_DATA[i][1]), 0);
			long end_time = System.currentTimeMillis();
			System.out.print("test_getGodCountForShunZi  i:" + i + ", " + TEST_NEEDGOD_DATA[i][0] + ", " + needGodCount + ", durtion : " + (end_time - start_time) + "\n");
			assertEquals((int)Integer.valueOf(TEST_NEEDGOD_DATA[i][0]), needGodCount);
		}
	}
	
	public void test_isHuEnable_Jiang() {
		for (int i = 0; i < TEST_HUJIANG_DATA.length; i++) {
			mUnitData = new MahjUnitData();
			String data = TEST_HUJIANG_DATA[i][1];
			int godCount = Integer.valueOf(TEST_HUJIANG_DATA[i][0]);
			boolean expected = Boolean.valueOf(TEST_HUJIANG_DATA[i][2]);
			long start_time = System.currentTimeMillis();
			boolean isHu = mUnitData.isHuEnable(TestUtil.getMahjIndexs(data), godCount);
			long end_time = System.currentTimeMillis();
			System.out.print("test_isHuEnable_Jiang  i:" + i + ", expected:" + expected + ", isHu:" + isHu + ", durtion : " + (end_time - start_time) + "\n");
			assertEquals(expected, isHu);
		}
	}
}
