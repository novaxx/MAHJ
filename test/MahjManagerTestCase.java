package test;
import org.junit.Test;

import junit.framework.TestCase;
import nova.common.game.mahjong.MahjManager;

public class MahjManagerTestCase extends TestCase {

	private MahjManager mManager;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		if (mManager == null) {
			mManager = new MahjManager();
		}
	}
	
	@Test
	public void testInitDatas() {
		mManager.initDatas();
		assertEquals(84, mManager.getMahjDatas().size());
		assertEquals(13, mManager.getPlayerDatas().get(0).getDatas().size());
		assertEquals(13, mManager.getPlayerDatas().get(1).getDatas().size());
		assertEquals(13, mManager.getPlayerDatas().get(2).getDatas().size());
		assertEquals(13, mManager.getPlayerDatas().get(3).getDatas().size());
	}

}
