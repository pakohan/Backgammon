package controllers.de.htwg.upfaz.backgammon.controller;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import controllers.de.htwg.upfaz.backgammon.gui.Constances;

public class CoreTest {

	private Core c;
	private UUID u;
	
	private static Injector inj = Guice.createInjector(new TestModule());
	
	@Before
	public void setUp() throws Exception {
		c = inj.getInstance(Core.class);
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
		
		assertTrue(c.click(0));
		assertTrue(c.click(c.getDiceToDraw(0)));
		
		c.setWinner(0);
		assertTrue(c.click(0));
		
		c.getGameMap().getField(GameMap.FIELD_END_WHITE).setNumberStones(Constances.STONES_TO_WIN);
		assertFalse(c.click(26));
		
		c.getCurrentPlayer().changeCurrentPlayer();
		assertFalse(c.click(0));
		
		assertTrue(c.click(12));
		assertFalse(c.click(22));
		
		assertTrue(c.click(12));
		assertTrue(c.click(c.getDiceToDraw(0)));
		
		assertFalse(c.click(26));
		
		c.getGameMap().getField(GameMap.FIELD_END_BLACK).setNumberStones(Constances.STONES_TO_WIN);
		assertFalse(c.click(26));
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
