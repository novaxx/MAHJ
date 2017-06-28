package nova.common.game.mahjong.util;

public class TestMahjConstant {

	private static boolean mIsDebug = false;
	private static int mDebugType = 0;
	
	// MAHJ_ELEMENTS_DEBUG_1 : 起手杆
	public final static int[][] MAHJ_ELEMENTS_DEBUG_1 = {
			{1,1,1,1,2,3,4,5,6,7,8,9,9},
			{11,11,11,11,12,13,14,15,16,17,18,19,19},
			{21,21,21,21,22,23,24,25,26,27,28,29,29},
			{2,3,4,5,6,7,8,9,12,13,14,15,16},
			{2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,12,12,13,13,14,14,15,15,16,16,17,17,17,18,18,18,19,19,22,22,22,23,23,23,24,24,24,25,25,25,26,26,26,27,27,27,28,28,28,29,29,31,31,31,31,32,32,32,32,33,33,33,33,34,34,34,34,41,41,41,41,42,42,42,42,43,43,43,43}
	};
	
	public static void setDebug(int debugType) {
		mIsDebug = true;
		mDebugType = debugType;
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
		}
		
		return null;
	}
}
