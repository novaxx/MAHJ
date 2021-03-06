package nova.common.game.mahjong.util;

public class MahjConstant {
	
	public static final int MAHJ_MATCH_GUO = 0;
	public static final int MAHJ_MATCH_CHI = 1;
	public static final int MAHJ_MATCH_PENG = 2;
	public static final int MAHJ_MATCH_GANG = 4;
	public static final int MAHJ_MATCH_TING = 8;
	public static final int MAHJ_MATCH_HU = 16;
	
	public final static int[][] MAHJ_ARRS = {
			{1,2,3,4,5,6,7,8,9},
			{11,12,13,14,15,16,17,18,19},
			{21,22,23,24,25,26,27,28,29},
			{31,32,33,34},
			{41,42,43},
	};
	
	public final static int[] MAH_JONG_ELEMENTS = {
		/*萬*/
		1,  2,  3,  4,  5,  6,  7,  8,  9,
		1,  2,  3,  4,  5,  6,  7,  8,  9,
		1,  2,  3,  4,  5,  6,  7,  8,  9,
		1,  2,  3,  4,  5,  6,  7,  8,  9,
		/*条*/
		11, 12, 13, 14, 15, 16, 17, 18, 19,
		11, 12, 13, 14, 15, 16, 17, 18, 19,
		11, 12, 13, 14, 15, 16, 17, 18, 19,
		11, 12, 13, 14, 15, 16, 17, 18, 19,
		/*筒*/
		21, 22, 23, 24, 25, 26, 27, 28, 29,
		21, 22, 23, 24, 25, 26, 27, 28, 29,
		21, 22, 23, 24, 25, 26, 27, 28, 29,
		21, 22, 23, 24, 25, 26, 27, 28, 29,
		/*东南西北*/
		31, 32, 33, 34,
		31, 32, 33, 34,
		31, 32, 33, 34,
		31, 32, 33, 34,
		/*中發白*/
		41, 42, 43,
		41, 42, 43,
		41, 42, 43,
		41, 42, 43,
	};

	/**
	 * mahjong count:
	 * 9 * 4 * 3 + 4 * 4 + 3* 4 = 136
	 */
	public static final int MAH_JONG_COUNT = MAH_JONG_ELEMENTS.length;
	
}
