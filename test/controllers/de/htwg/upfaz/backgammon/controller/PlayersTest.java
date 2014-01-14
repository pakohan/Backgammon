package controllers.de.htwg.upfaz.backgammon.controller;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controllers.de.htwg.upfaz.backgammon.entities.IPlayer;
import controllers.de.htwg.upfaz.backgammon.entities.Player;

public class PlayersTest {
	
	private Players p;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		p = new Players();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPlayers() {
		assertNotNull(new Players());
	}

	@Test
	public void testGetCurrentPlayer() {
		assertNotNull(p.getCurrentPlayer());
	}

	@Test
	public void testChangeCurrentPlayer() {
		Player oldPlayer = (Player) p.getCurrentPlayer();
		p.changeCurrentPlayer();
		Player newPlayer = (Player) p.getCurrentPlayer();
//		assertNotEquals(newPlayer, oldPlayer);
	}

	@Test
	public void testGetColor() {
		assertNotNull(p.getClass());
	}

	@Test
	public void testToString() {
		int currentPlayer = p.getColor();
		if (currentPlayer == 0) {
			assertTrue(p.toString().contains("white"));
		} else {
			assertTrue(p.toString().contains("black"));
		}
	}

	@Test
	public void testSetCurrentPlayer() {
		
	}

}
