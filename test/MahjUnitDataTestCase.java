package test;

import junit.framework.TestCase;
import nova.common.game.mahjong.data.MahjData;
import nova.common.game.mahjong.data.MahjUnitData;

public class MahjUnitDataTestCase extends TestCase {
	private static final String[] TEST_DATA = {
			"12,12,12,11,13,13,15",
			"33,33,33,32,31,31"
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
	
	public void test_matchType_empty() {
		mUnitData = new MahjUnitData();
		assertEquals(0, mUnitData.getMatchType(new MahjData(13)));
	}
	
	public void test_matchType_gan() {
		mUnitData = new MahjUnitData();
		mUnitData.addAll(TestUtil.getMahjDatas(TEST_DATA[0]));
		mUnitData.updateUnitInfo();
		assertEquals(110, mUnitData.getMatchType(new MahjData(12)));
	}
	
	public void test_matchType_peng() {
		mUnitData = new MahjUnitData();
		mUnitData.addAll(TestUtil.getMahjDatas(TEST_DATA[0]));
		mUnitData.updateUnitInfo();
		assertEquals(10, mUnitData.getMatchType(new MahjData(13)));
	}
	
	public void test_matchType_feng_gan() {
		mUnitData = new MahjUnitData();
		mUnitData.addAll(TestUtil.getMahjDatas(TEST_DATA[1]));
		mUnitData.updateUnitInfo();
		assertEquals(110, mUnitData.getMatchType(new MahjData(33)));
	}
	
	public void test_matchType_feng_peng() {
		mUnitData = new MahjUnitData();
		mUnitData.addAll(TestUtil.getMahjDatas(TEST_DATA[1]));
		mUnitData.updateUnitInfo();
		assertEquals(10, mUnitData.getMatchType(new MahjData(31)));
	}
}
