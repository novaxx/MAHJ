package nova.common.room.data;

public class PlayerInfo {

	private int id;
	private String openId;
	private String name;
	private String head;
	private byte[] headdatas;
	/**
	 *  0:girl, 1:boy
	 */
	private int sex;
	private int vip;
	private int lv;
	private int gold;
	private int crys;
	/**
	 * 0: player 1: android
	 */
	private int type;

	public PlayerInfo() {

	}

	public PlayerInfo(int id, String name, String head, int sex) {
		this.id = id;
		this.name = name;
		this.head = head;
		this.sex = sex;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
	
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	public String getOpenId() {
		return this.openId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getHead() {
		return this.head;
	}
	
	public void setHeaddatas(byte[] headdatas) {
		this.headdatas = headdatas;
	}
	
	public byte[] getHeaddatas() {
		return this.headdatas;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getSex() {
		return sex;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}
	
	public int getLv() {
		return this.lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}
	
	public int getCrys() {
		return this.crys;
	}

	public void setCrys(int crys) {
		this.crys = crys;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return this.type;
	}

	public boolean isNormalPlayer() {
		return this.type == 0;
	}
}
