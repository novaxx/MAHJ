package nova.common.game.mahjong.data;

import nova.common.game.mahjong.util.MahjGameCommand.FanType;

public class FanData {
	private int type;
	private int num;
	
	public FanData(int type, int num) {
		this.type = type;
		this.num = num;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	public int getNum() {
		return this.num;
	}
	
	public String getFanDisplayType() {
		if (this.type < FanType.FAN_DISPLAY_TYPE.length) {
			return FanType.FAN_DISPLAY_TYPE[this.type];
		} else {
			return "";
		}
	}
}
