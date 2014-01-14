package controllers.de.htwg.upfaz.backgammon.controller;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CoreTest {

	private Core c;
	private UUID u;
	
	@Before
	public void setUp() throws Exception {
		c = new Core();
		u = c.createGame();
	}

	@After
	public void tearDown() throws Exception {
		c = null;
	}

	@Test
	public void testCreateGame() {
		u = c.createGame();
		assertNotNull(u);
		assertNotNull(c.getGameMap());
	}

	@Test
	public void testLoadGame() {
		c.loadGame(u);
		
	}

	@Test
	public void testGetGameMap() {
		assertNotNull(c.getGameMap());
	}

	@Test
	public void testGetCurrentPlayer() {
		assertNotNull(c.getCurrentPlayer());
	}

	@Test
	public void testGetDiceToDraw() {
		assertNotNull(c.getDiceToDraw(0));
	}

	@Test
	public void testClick() {
		assertTrue(c.click(0));
		assertFalse(c.click(0));
	}

	@Test
	public void testGetWinner() {
		assertNotNull(c.getWinner());
	}

	@Test
	public void testSetWinner() {
		c.setWinner(0);
		assertEquals(0, c.getWinner());
	}

}
