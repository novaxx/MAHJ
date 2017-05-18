package test;

import junit.framework.TestCase;
import nova.common.game.mahjong.data.MahjData;
import nova.common.game.mahjong.data.MahjGroupData;

public class MahjGroupDataTestCase extends TestCase {

	private static final String[] TEST_DATA = {
			"43,43,42,42,41,41,12,12,12,11,13,14,15",
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
	
	public void testOutData() {
		mGroupData = new MahjGroupData(0, TestUtil.getMahjDatas(TEST_DATA[0]));
		mGroupData.setLatestData(new MahjData(3));
		assertEquals(3, mGroupData.getAutoOutData().getIndex());
	}
}
