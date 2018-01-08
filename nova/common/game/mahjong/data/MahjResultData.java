package nova.common.game.mahjong.data;

import java.util.ArrayList;

public class MahjResultData {

	private ArrayList<String> orders;
	private int totalFan;
	private int balance;
	private ArrayList<FanData> fanDatas = new ArrayList<FanData>();
	
	public void setOrders(ArrayList<String> orders) {
		this.orders = orders;
	}
	
	public ArrayList<String> getOrders() {
		return this.orders;
	}
	
	public void setTotalFan(int totalFan) {
		this.totalFan = totalFan;
	}
	
	public int getTotalFan() {
		return this.totalFan;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public int getBalance() {
		return this.balance;
	}
	
	public void setFanDatas(ArrayList<FanData> fanDatas) {
		this.fanDatas = fanDatas;
	}
	
	public ArrayList<FanData> getFanDatas() {
		return this.fanDatas;
	}
	
	public void addFanData(FanData fanData) {
		this.fanDatas.add(fanData);
	}
	
	public void addFanDatas(ArrayList<FanData> fanDatas) {
		this.fanDatas.addAll(fanDatas);
	}
}
