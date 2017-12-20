package test;

import java.util.ArrayList;

import junit.framework.TestCase;
import nova.common.game.mahjong.data.MahjData;
import nova.common.game.mahjong.data.MahjGroupData;

public class MahjGroupDataTestCase extends TestCase {

	private static final String[] TEST_DATA = {
			"43,43,42,42,41,41,12,12,12,11,13,14,15",
	};
	
	private static final String[][] TEST_MATCH_DATA = {
			/**{"god", "operateType", "matchDatas", "datas", "outdata", "expected"}**/
			{"16", "0", "", "43,42,41,33,32,31,29,28,27,27,27,25,24", "27", "6"},
			{"16", "0", "", "43,42,41,33,32,31,29,28,27,27,26,25,24", "27", "2"},
			{"16", "0", "43,43,43", "43,42,41,33,32,31,29,28,27,27", "27", "2"},
			{"16", "0", "14,14,14", "43,42,41,33,32,31,29,28,27,27", "27", "0"},
			{"16", "0", "16,16,16", "43,42,41,33,32,31,29,27,27,27", "27", "6"},
			{"16", "0", "33,33,33,13,13,13", "43,42,41,29,27,27,27", "27", "0"},
			{"16", "0", "33,33,33,16,16,16", "43,42,41,29,27,27,27", "27", "6"},
			{"16", "0", "33,33,33,27,27,27,27", "43,43,42,41,26,26", "43", "2"},
			{"16", "0", "3,3,3,27,27,27,27", "43,43,42,41,26,26", "43", "2"},
			{"16", "8", "3,3,3,27,27,27,27", "43,43,42,41,26,26", "43", "0"},
			{"16", "8", "3,3,3,27,27,27,27", "43,43,43,41,26,26", "43", "4"},
	};
	
	private static final String[][] TEST_MATCH_LATEST_DATA = {
			/**{"god", "matchDatas", "datas", "latestdata", "expected"}**/
			{"16", "", "43,42,41,33,32,31,29,28,27,27,27,25,24", "27", "4"},
			{"16", "27,27,27", "43,42,41,33,32,31,29,28,25,24", "27", "4"},
	};
	
	private static final String[][] TEST_HU_DATA = {
		/**{"god", "matchDatas", "datas", "latest", "operatetype", "expected"}**/
		{"21", "", "4,4,3,3,3,3,2,2,2,2,1,1,1", "1", "8", "true"},
		{"32", "", "19,19,19,18,17,16,15,14,13,12,11,11,11", "11", "8", "true"},
		{"32", "", "19,19,19,18,17,16,15,14,13,12,11,11,11", "12", "8", "true"},
		{"32", "", "19,19,19,18,17,16,15,14,13,12,11,11,11", "15", "8", "true"},
		{"21", "", "21,19,19,19,18,17,16,15,14,13,12,11,11", "11", "8", "true"},
		{"24", "", "43,42,41,33,32,31,27,26,25,23,23,15,15", "23", "8", "false"},
		{"24", "", "43,42,41,33,31,27,26,25,24,23,23,23,15", "15", "8", "false"},
		{"21", "43,43,43,42,42,42,42", "33,32,31,21,16,15,14", "11", "8", "true"},
		{"21", "43,43,43,42,42,42,42", "33,32,31,16,15,14,13", "11", "8", "false"},
		{"15", "43,43,43", "34,32,31,26,25,24,23,22,21,15", "41", "8", "true"},
		/*实际场景*/
		{"33", "19,19,19", "43,42,41,33,32,31,18,17,16,15", "15", "8", "true"},
		{"19", "29,29,29", "42,42,28,27,26,25,24,23,23,19", "23", "0", "false"},
	};
	
	private static final String[][] TEST_TING_DATA = {
			/**{"god", "matchDatas", "datas", "expected"}**/
			{"13", "", "43,42,41,33,33,33,9,9,7,6,5,4,3", "2,5,8,13"},
			{"32", "", "43,42,41,33,33,33,19,19,7,6,5,4,3", ""},
			{"32", "", "43,42,41,33,33,33,19,17,15,14,13,13,12", ""},
			{"32", "", "43,42,41,33,33,33,19,18,17,15,14,13,12", "12,15,32"},
			{"32", "", "19,19,19,18,17,16,15,14,13,12,11,11,11", "11,12,13,14,15,16,17,18,19,32"},
			{"15", "43,43,43,27,27,27,27", "34,32,31,25,25,23,23", "23,25,15"},
			{"15", "43,43,43,27,27,27,27", "34,32,31,25,24,15,15", "21,22,23,24,25,26,28,29,31,32,33,34,41,42,43,15"},
			{"15", "43,43,43", "34,32,31,26,25,24,23,22,21,15", "21,22,23,24,25,26,27,28,29,31,32,33,34,41,42,43,15"},
			/*实际场景*/
			{"23", "14,14,14", "42,42,34,34,34,33,33,16,15,14", "33,42,23"},
			{"33", "19,19,19", "43,42,41,33,32,31,18,17,16,15", "15,18,33,34"},
			{"19", "29,29,29", "42,42,28,27,26,25,24,23,23,19", "21,22,23,24,25,26,29,42,19"},
	};
	
	private static final String[][] TEST_OPERATE_GANG_DATA = {
			/**{"god", "matchDatas", "datas", "lastestdata", "expected"}**/
			{"28", "", "42,42,34,34,34,33,33,16,15,14,14,14", "14", "9"},
			/*实际场景*/
			{"6", "", "43,42,41,33,32,17,16,15,11,11,11,6,6", "11", "10"},
			{"6", "11,11,11", "43,42,41,33,32,17,16,15,6,6", "11", "10"},
			{"26", "22,22,22,28,28,28,29,29,29", "25,26,31,32", "29", "4"},
	};
	
	private MahjGroupData mGroupData;
	
	public void test_updateMatchType() {
		for (int i = 0; i < TEST_MATCH_DATA.length; i++) {
			int godIndex = Integer.valueOf(TEST_MATCH_DATA[i][0]);
			int operate = Integer.valueOf(TEST_MATCH_DATA[i][1]);
			String matchDatas = TEST_MATCH_DATA[i][2];
			String datas = TEST_MATCH_DATA[i][3];
			MahjData outData = new MahjData(Integer.valueOf(TEST_MATCH_DATA[i][4]));
			int expected = Integer.valueOf(TEST_MATCH_DATA[i][5]);
			mGroupData = new MahjGroupData(0, TestUtil.getMahjDatas(datas));
			mGroupData.updateGodData(godIndex);
			mGroupData.setOperateType(operate);
			mGroupData.setMatchDatas(TestUtil.getMahjDatas(matchDatas));
			int matchType = mGroupData.updateMatchType(outData);
			assertEquals(expected, matchType);
		}
	}
	
	public void test_updateMatchTypeForGetMahj() {
		for (int i = 0; i < TEST_MATCH_LATEST_DATA.length; i++) {
			int godIndex = Integer.valueOf(TEST_MATCH_LATEST_DATA[i][0]);
			String matchDatas = TEST_MATCH_LATEST_DATA[i][1];
			String datas = TEST_MATCH_LATEST_DATA[i][2];
			MahjData latestData = new MahjData(Integer.valueOf(TEST_MATCH_LATEST_DATA[i][3]));
			int expected = Integer.valueOf(TEST_MATCH_LATEST_DATA[i][4]);
			mGroupData = new MahjGroupData(0, TestUtil.getMahjDatas(datas));
			mGroupData.updateGodData(godIndex);
			mGroupData.setMatchDatas(TestUtil.getMahjDatas(matchDatas));
			mGroupData.setLatestData(latestData);
			int matchType = mGroupData.updateMatchTypeForGetMahj();
			assertEquals(expected, matchType);
		}
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
			int operateType = Integer.valueOf(TEST_HU_DATA[i][4]);
			boolean expected = Boolean.valueOf(TEST_HU_DATA[i][5]);
			mGroupData = new MahjGroupData(0, TestUtil.getMahjDatas(datas));
			mGroupData.updateGodData(godIndex);
			mGroupData.setMatchDatas(TestUtil.getMahjDatas(matchDatas));
			mGroupData.setLatestData(latestData);
			mGroupData.setOperateType(operateType);
			mGroupData.setOuted(true);
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
	
	public void test_OperateGangData() {
		for (int i = 0; i < TEST_OPERATE_GANG_DATA.length; i++) {
			int godIndex = Integer.valueOf(TEST_OPERATE_GANG_DATA[i][0]);
			String matchDatas = TEST_OPERATE_GANG_DATA[i][1];
			String datas = TEST_OPERATE_GANG_DATA[i][2];
			MahjData lastestData = new MahjData(Integer.valueOf(TEST_OPERATE_GANG_DATA[i][3]));
			int expected = Integer.valueOf(TEST_OPERATE_GANG_DATA[i][4]);
			mGroupData = new MahjGroupData(0, TestUtil.getMahjDatas(datas));
			mGroupData.updateGodData(godIndex);
			mGroupData.setMatchDatas(TestUtil.getMahjDatas(matchDatas));
			mGroupData.setLatestData(lastestData);
			mGroupData.operateGangData(Integer.valueOf(TEST_OPERATE_GANG_DATA[i][3]));
			int latestSize = mGroupData.getLatestData() != null ? 1 : 0;
			assertEquals(expected, mGroupData.getDatas().size() + latestSize);
		}
	}
}
