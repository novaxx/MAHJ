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
}
