package nova.common.game.mahjong.data;

import nova.common.game.mahjong.util.MahjUtil;

public class MahjData {

	private int index;
	/**
	 * 0:萬 1:条 2:筒 3:东/南/西/北 4:中发白
	 */
	private int color;
	private int face;
	
	public MahjData(int index) {
		this.index = index;
		this.color = MahjUtil.getMahjColr(this.index);
		this.face = MahjUtil.getMahjFace(this.index);
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public int getColor() {
		return this.color;
	}
	
	public int getFace() {
		return this.face;
	}
}
