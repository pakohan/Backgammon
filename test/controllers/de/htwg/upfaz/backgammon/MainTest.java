package controllers.de.htwg.upfaz.backgammon;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainTest {
	
	private Main m;

	@Before
	public void setUp() throws Exception {
		m = new Main();
	}

	@After
	public void tearDown() throws Exception {
		m = null;
	}

	@Test
	public void testMain() {
		m.main(null);
	}

}
