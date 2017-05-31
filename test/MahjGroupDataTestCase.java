package test;

import java.util.ArrayList;

import junit.framework.TestCase;
import nova.common.game.mahjong.data.MahjData;
import nova.common.game.mahjong.data.MahjGroupData;
import nova.common.game.mahjong.util.MahjConstant;

public class MahjGroupDataTestCase extends TestCase {

	private static final String[] TEST_DATA = {
			"43,43,42,42,41,41,12,12,12,11,13,14,15",
	};
	
	private static final String[][] TEST_HU_DATA = {
		/**{"god", "matchDatas", "datas", "latest", "expected"}**/
		{"21", "", "4,4,3,3,3,3,2,2,2,2,1,1,1", "1", "true"},
		{"32", "", "19,19,19,18,17,16,15,14,13,12,11,11,11", "11", "true"},
		{"32", "", "19,19,19,18,17,16,15,14,13,12,11,11,11", "12", "true"},
		{"32", "", "19,19,19,18,17,16,15,14,13,12,11,11,11", "15", "true"},
		{"21", "", "21,19,19,19,18,17,16,15,14,13,12,11,11", "11", "true"},
		{"24", "", "43,42,41,33,32,31,27,26,25,23,23,15,15", "23", "false"},
		{"24", "", "43,42,41,33,31,27,26,25,24,23,23,23,15", "15", "false"},
		{"21", "43,43,43,42,42,42,42", "33,32,31,21,16,15,14", "11", "true"},
		{"21", "43,43,43,42,42,42,42", "33,32,31,16,15,14,13", "11", "false"},
	};
	
	private static final String[][] TEST_TING_DATA = {
			/**{"god", "matchDatas", "datas", "expected"}**/
			{"32", "", "19,19,19,18,17,16,15,14,13,12,11,11,11", "11,12,13,14,15,16,17,18,19,32"},
			{"13", "", "43,42,41,33,33,33,9,9,7,6,5,4,3", "2,5,8,13"},
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
		long start_time = System.currentTimeMillis();
		for (int i = 0; i < TEST_HU_DATA.length; i++) {
			int godIndex = Integer.valueOf(TEST_HU_DATA[i][0]);
			String matchDatas = TEST_HU_DATA[i][1];
			String datas = TEST_HU_DATA[i][2];
			MahjData latestData = new MahjData(Integer.valueOf(TEST_HU_DATA[i][3]));
			boolean expected = Boolean.valueOf(TEST_HU_DATA[i][4]);
			mGroupData = new MahjGroupData(0, TestUtil.getMahjDatas(datas));
			mGroupData.updateGodData(godIndex);
			mGroupData.setMatchDatas(TestUtil.getMahjDatas(matchDatas));
			mGroupData.setLatestData(latestData);
			mGroupData.setOperateType(MahjConstant.MAHJ_MATCH_TING);
			boolean isHu = mGroupData.isHuEnable();
			assertEquals(expected, isHu);
		}
		long end_time = System.currentTimeMillis();
		System.out.print("test_IsHuEnable--count:" + TEST_HU_DATA.length + ",total:" + (end_time - start_time) + ",each:" + (end_time - start_time)/TEST_HU_DATA.length + "\n");
	}
	
	public void test_GetTingDatas() {
		long start_time = System.currentTimeMillis();
		for (int i = 0; i < TEST_TING_DATA.length; i++) {
			int godIndex = Integer.valueOf(TEST_TING_DATA[i][0]);
			String matchDatas = TEST_TING_DATA[i][1];
			String datas = TEST_TING_DATA[i][2];
			ArrayList<Integer> expectedDatas = TestUtil.getMahjIndexs(TEST_TING_DATA[i][3]);
			mGroupData = new MahjGroupData(0, TestUtil.getMahjDatas(datas));
			mGroupData.updateGodData(godIndex);
			mGroupData.setMatchDatas(TestUtil.getMahjDatas(matchDatas));
			ArrayList<Integer> tingDatas = mGroupData.getTingDatas();
			System.out.print("test_GetTingDatas , ting : " + tingDatas + "\n");
			assertEquals(expectedDatas, tingDatas);
		}
		long end_time = System.currentTimeMillis();
		System.out.print("test_GetTingDatas--count:" + TEST_TING_DATA.length + ",total:" + (end_time - start_time) + ",each:" + (end_time - start_time)/TEST_TING_DATA.length + "\n");
	}
}
