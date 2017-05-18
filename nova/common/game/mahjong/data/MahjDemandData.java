package nova.common.game.mahjong.data;

public class MahjDemandData {

	private int mIndex;
	private boolean mIsGodEnable;
	
	public MahjDemandData(int index, boolean isGodEnable) {
		mIndex = index;
		mIsGodEnable = isGodEnable;
	}
	
	public int getIndex() {
		return mIndex;
	}
	
	public boolean isGodEnable() {
		return mIsGodEnable;
	}
}
