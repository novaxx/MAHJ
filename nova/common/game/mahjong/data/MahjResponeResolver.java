package nova.common.game.mahjong.data;

import java.util.ArrayList;
import java.util.HashMap;

import nova.common.game.mahjong.data.MahjResponeData.MahjPartData;

public class MahjResponeResolver {
	public static HashMap<Integer, MahjGroupData> getGroupDatasForResponeData(MahjResponeData responeData) {
		HashMap<Integer, MahjGroupData> groupDatas = new HashMap<Integer, MahjGroupData>();
		for (int i = 0; i < 4; i++) {
			if (responeData.getPartDatas().get(String.valueOf(i)) == null) {
				continue;
			}
			
			MahjPartData partData = responeData.getPartDatas().get(String.valueOf(i));
			ArrayList<MahjData> datas = new ArrayList<MahjData>();
			for (int index : partData.getDatas()) {
				datas.add(new MahjData(index));
			}
			MahjGroupData groupData = new MahjGroupData(i, datas);
			groupData.setLatestData(new MahjData(partData.getLatestData()));
			ArrayList<MahjData> outdatas = new ArrayList<MahjData>();
			if (partData.getOutDatas() != null && partData.getOutDatas().size() > 0) {
				for (int index : partData.getOutDatas()) {
					outdatas.add(new MahjData(index));
				}
			}
			groupData.setOutDatas(outdatas);
			ArrayList<MahjData> matchdatas = new ArrayList<MahjData>();
			if (partData.getMatcheDatas() != null && partData.getMatcheDatas().size() > 0) {
				for (int index : partData.getMatcheDatas()) {
					matchdatas.add(new MahjData(index));
				}
			}
			groupData.setOuted(partData.isOuted());
			groupData.setMatchDatas(matchdatas);
			groupData.updateGodData(responeData.getGod());
			groupData.setOperateType(partData.getOperate());
			groupDatas.put(i, groupData);
		}
		
		return groupDatas;
	}
}
