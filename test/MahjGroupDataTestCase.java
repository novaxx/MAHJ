package test;

import junit.framework.TestCase;
import nova.common.game.mahjong.data.MahjData;
import nova.common.game.mahjong.data.MahjGroupData;

public class MahjGroupDataTestCase extends TestCase {

	private static final String[] TEST_DATA = {
			"43,43,42,42,41,41,12,12,12,11,13,14,15",
	};
	
	private static final String[][] TEST_HU_DATA = {
		{"32", "19,19,19,18,17,16,15,14,13,12,11,11,11,11", "true"},
		{"21", "21,19,19,19,18,17,16,15,14,13,12,11,11,11", "true"},
		{"24", "43,42,41,33,32,31,27,26,25,23,23,23,15,15", "false"},
	};
	
	private MahjGroupData mGroupData;
	
	public void testMatchType() {
		mGroupData = new MahjGroupData(0, TestUtil.getMahjDatas(TEST_DATA[0]));
		mGroupData.updateMatchType(new MahjData(12));
		assertEquals(110, mGroupData.getMatchType());
	}
	
	public void test_MatchType_feng() {
		mGroupData = new MahjGroupData(0, TestUtil.getMahjDatas(TEST_DATA[0]));
		mGroupData.updateMatchType(new MahjData(43));
		assertEquals(10, mGroupData.getMatchType());
	}
	
	public void test_OutData() {
		mGroupData = new MahjGroupData(0, TestUtil.getMahjDatas(TEST_DATA[0]));
		mGroupData.setLatestData(new MahjData(3));
		assertEquals(3, mGroupData.getAutoOutData().getIndex());
	}
	
	public void test_IsHuEnable() {
		for (int i = 0; i < TEST_HU_DATA.length; i++) {
			String data = TEST_HU_DATA[i][1];
			int godIndex = Integer.valueOf(TEST_HU_DATA[i][0]);
			boolean expected = Boolean.valueOf(TEST_HU_DATA[i][2]);
			mGroupData = new MahjGroupData(0, TestUtil.getMahjDatas(data));
			mGroupData.updateGodData(godIndex);
			boolean isHu = mGroupData.isHuEnable();
			assertEquals(expected, isHu);
		}
	}
}
