package test;

import junit.framework.TestCase;
import nova.common.game.mahjong.data.MahjData;
import nova.common.game.mahjong.data.MahjGroupData;
import nova.common.game.mahjong.util.MahjResultUtil;

public class MahjResultUtilTestCase extends TestCase {
	
	private static final String[][] TEST_FAN_FOR_WINNER = {
			/**{"god", "outed", "matchDatas", "datas", "lastestdata", "expected"}**/
			/*四方大发*/
			{"28", "true", "31,31,31,31,32,32,32,32,33,33,33,33,34,34,34,34", "42", "42", "168"},
			{"28", "true", "31,31,31,32,32,32,33,33,33,34,34,34", "42", "42", "168"},
			{"28", "true", "31,31,31", "32,32,32,33,33,33,34,34,42,42", "34", "168"},
			/*天胡*/
			{"28", "false", "", "11,12,13,14,14,14,16,17,18,31,32,33,42", "42", "168"},
			/*大四喜*/
			{"28", "true", "31,31,31,31,32,32,32,32,33,33,33,33,34,34,34,34", "12", "12", "88"},
			{"28", "true", "31,31,31,32,32,32,33,33,33,34,34,34", "12", "12", "88"},
			{"28", "true", "", "31,31,31,32,32,32,33,33,33,34,34,12,12", "34", "88"},
			/*大三元*/
			{"28", "true", "41,41,41,41,42,42,42,42,43,43,43,43", "12,13,14,15", "12", "88"},
			{"28", "true", "41,41,41,42,42,42,43,43,43", "12,13,14,15", "12", "88"},
			{"28", "true", "", "12,13,14,15,15,41,41,41,42,42,42,43,43", "43", "88"},
			/*九莲灯*/
			// {"28", "true", "", "11,11,11,12,13,14,15,16,17,18,19,19,19", "17", "88"},
			/*连七对*/
			{"28", "true", "", "11,11,12,12,13,13,14,15,15,16,16,17,17", "14", "88"},
			/*十八学士*/
			{"28", "true", "13,13,13,13,15,15,15,15,17,17,17,17,31,31,31,31", "14", "14", "88"},
			/*七对*/
			{"28", "true", "", "11,11,12,12,13,13,31,15,15,16,16,17,17", "31", "24"},
			/*清一色*/
			{"28", "true", "31,31,31", "11,12,13,15,16,17,19,19,33,33", "33", "11"},
			/*门前清*/
			{"28", "true", "", "11,12,13,15,16,17,19,19,31,31,31,33,33", "33", "13"},
			/*杆*/
			{"28", "true", "13,13,13,13,15,15,15,15", "14,14,16,17,18,31,31", "31", "13"},
			/*有风*/
			{"28", "true", "31,31,31", "11,12,13,15,16,17,19,19,41,42", "43", "12"},
			//{"28", "true", "13,13,13", "12,12,31,31,32,32,32,33,33", "33", "13"},
			{"28", "true", "13,13,13", "12,12,31,31,32,32,33,33,34,34", "31", "13"},
			{"28", "true", "13,13,13", "12,12,31,31,32,33,34,41,42,43", "31", "13"},
			/*单调*/
			{"28", "true", "31,31,31", "28,32,33,34,41,42,43", "12", "14"},
	};
	
	private static final String[][] TEST_FAN_FOR_LOSER = {
			/**{"god", "outed", "matchDatas", "datas", "lastestdata", "expected"}**/
			{"28", "true", "", "11,12,14,15,15,16,17,18,18,31,32,33", "34", "0"},
			{"28", "true", "33,33,33,33", "11,12,14,15,15,16,17,18,18", "34", "1"},
			{"28", "true", "13,13,13,33,33,33,33", "15,15,16,17,18,18", "34", "1"},
			{"28", "true", "13,13,13,13,33,33,33", "15,15,16,17,18,18", "34", "1"},
			{"28", "true", "13,13,13,13,33,33,33,33", "15,15,16,17,18,18", "34", "2"},
	};
	
	public void test_getResultForWinner() {
		for (int i = 0; i < TEST_FAN_FOR_WINNER.length; i++) {
			int godIndex = Integer.valueOf(TEST_FAN_FOR_WINNER[i][0]);
			boolean isOuted = Boolean.valueOf(TEST_FAN_FOR_WINNER[i][1]);
			String matchDatas = TEST_FAN_FOR_WINNER[i][2];
			String datas = TEST_FAN_FOR_WINNER[i][3];
			MahjData lastestData = new MahjData(Integer.valueOf(TEST_FAN_FOR_WINNER[i][4]));
			int expected = Integer.valueOf(TEST_FAN_FOR_WINNER[i][5]);
			MahjGroupData groupData = new MahjGroupData(0, TestUtil.getMahjDatas(datas));
			groupData.updateGodData(godIndex);
			groupData.setOuted(isOuted);
			groupData.setMatchDatas(TestUtil.getMahjDatas(matchDatas));
			groupData.setLatestData(lastestData);
			assertEquals(expected, MahjResultUtil.getResultForWinner(groupData).getTotalFan());
		}
	}
	
	public void test_getResultForLoser() {
		for (int i = 0; i < TEST_FAN_FOR_LOSER.length; i++) {
			int godIndex = Integer.valueOf(TEST_FAN_FOR_LOSER[i][0]);
			boolean isOuted = Boolean.valueOf(TEST_FAN_FOR_LOSER[i][1]);
			String matchDatas = TEST_FAN_FOR_LOSER[i][2];
			String datas = TEST_FAN_FOR_LOSER[i][3];
			MahjData lastestData = new MahjData(Integer.valueOf(TEST_FAN_FOR_LOSER[i][4]));
			int expected = Integer.valueOf(TEST_FAN_FOR_LOSER[i][5]);
			MahjGroupData groupData = new MahjGroupData(0, TestUtil.getMahjDatas(datas));
			groupData.updateGodData(godIndex);
			groupData.setOuted(isOuted);
			groupData.setMatchDatas(TestUtil.getMahjDatas(matchDatas));
			groupData.setLatestData(lastestData);
			assertEquals(expected, MahjResultUtil.getResultForLoser(groupData).getTotalFan());
		}
	}
}
