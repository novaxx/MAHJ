package nova.common.game.mahjong.util;

public class TestMahjConstant {

	private static boolean mIsDebug = false;
	private static int mDebugType = 0;
	
	// MAHJ_ELEMENTS_DEBUG_1 : 起手杆/天胡
	public final static int[][] MAHJ_ELEMENTS_DEBUG_1 = {
			{1,1,1,1,2,3,4,5,6,7,8,9,9},
			{11,11,11,11,12,13,14,15,16,17,18,19,19},
			{21,21,21,21,22,23,24,25,26,27,28,29,29},
			{2,3,4,5,6,7,8,9,12,13,14,15,16},
			{2,2,3,3,4,4,5,5,6,6,7,7,8,8,9},
			{43}
	};
	
	// MAHJ_ELEMENTS_DEBUG_2 : 打牌碰杆
	public final static int[][] MAHJ_ELEMENTS_DEBUG_2 = {
			{1,1,1,2,3,4,5,6,7,8,8,9,9},
			{11,11,11,12,12,13,14,15,16,17,18,19,9},
			{21,21,21,22,22,23,24,25,26,27,28,29,1},
			{2,3,4,5,6,7,8,9,12,13,14,15,16},
			{12,12,13,13,14,14,15,15,16,16,17,17,18,18,19},
			{43}
	};
	
	// MAHJ_ELEMENTS_DEBUG_3 : 连续杆
	public final static int[][] MAHJ_ELEMENTS_DEBUG_3 = {
			{1,1,1,1,2,2,2,2,3,3,3,3,4},
			{11,12,13,14,15,16,17,18,19,21,22,23,24},
			{11,12,13,14,15,16,17,18,19,21,22,23,24},
			{11,12,13,14,15,16,17,18,19,21,22,23,24},
			{4,4,4,5,5,5,5},
			{43}
	};
	
	// MAHJ_ELEMENTS_DEBUG_4 : 听牌/胡牌
	public final static int[][] MAHJ_ELEMENTS_DEBUG_4 = {
			{43,42,41,34,32,31,1,2,3,4,5,6,7},
			{11,12,12,13,14,14,15,16,16,17,18,18,19},
			{11,12,12,13,14,14,15,16,16,17,18,18,19},
			{11,12,12,13,14,14,15,16,16,17,18,18,19},
			{21,22,23,24,7,7,7,8,8,9,9,9,9},
			{33}
	};
	
	/**
	 * @param debugType
	 * 1-起手杆 2-打牌碰杆 3-连续杆
	 */
	public static void setDebug(int debugType) {
		mIsDebug = true;
		mDebugType = debugType;
	}
	
	public static void cancelDebug() {
		mIsDebug = false;
		mDebugType = 0;
	}
	
	public static int getDebugType() {
		return mDebugType;
	}
	
	public static boolean isDebug() {
		return mIsDebug;
	}
	
	public static int[][] getDebugElements() {
		if (mDebugType == 1) {
			return MAHJ_ELEMENTS_DEBUG_1;
		} else if (mDebugType == 2) {
			return MAHJ_ELEMENTS_DEBUG_2;
		} else if (mDebugType == 3) {
			return MAHJ_ELEMENTS_DEBUG_3;
		} else if (mDebugType == 4) {
			return MAHJ_ELEMENTS_DEBUG_4;
		}
		
		return null;
	}
}
