package nova.common.game.mahjong.data;

import java.util.ArrayList;

public class MahjGameData {

	private int banker;
	private int current;
	private int lastout;
	private int god;
	private ArrayList<MahjData> datas;
	
	public void initDatas() {
		banker = 0;
		current = 0;
		lastout = -1;
	}
	
	public void setBanker(int banker) {
		this.banker = banker;
	}
	
	public int getBanker() {
		return this.banker;
	}
	
	public void setCurrent(int current) {
		if (current >= 4) {
			this.current = current - 4;
		} else {
			this.current = current;
		}
	}
	
	public int getCurrent() {
		return this.current;
	}
	
	public void setLastout(int lastout) {
		this.lastout = lastout;
	}
	
	public int getLastout() {
		return this.lastout;
	}
	
	public void clearLastout() {
		this.lastout = -1;
	}
	
	public void setGod(int god) {
		this.god = god;
	}
	
	public int getGod() {
		return this.god;
	}
	
	public void setDatas(ArrayList<MahjData> datas) {
		this.datas = datas;
	}
	
	public ArrayList<MahjData> getDatas() {
		return this.datas;
	}
}
