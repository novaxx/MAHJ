package nova.common.game.mahjong.util;

public class MahjUtil {

	public static int getMahjColr(int index) {
		return index / 10;
	}
	
	public static int getMahjFace(int index) {
		return index % 10;
	}
	
	public static boolean isGodMahj(int index, int god) {
		return index == god;
	}
}
