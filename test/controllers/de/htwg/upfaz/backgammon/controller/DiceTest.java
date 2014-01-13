package controllers.de.htwg.upfaz.backgammon.controller;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DiceTest {
	
	private Dice dice;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		dice = new Dice();
	}

	@After
	public void tearDown() throws Exception {
		dice = null;
	}

	@Test
	public void testDice() {
		assertNotNull(new Dice());
	}

	@Test
	public void testRollTheDice() {
		
	}

	@Test
	public void testHasTurnsLeft() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDiceToDraw() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDiceAt() {
		fail("Not yet implemented");
	}

	@Test
	public void testMove() {
		fail("Not yet implemented");
	}

	@Test
	public void testRenewJumpsEndPhase() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckDistance() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetValues() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetValues() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetValuesToDraw() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetValuesToDraw() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNumberTurns() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNumberTurnsLeft() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetNumberTurnsLeft() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetNumberTurns() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetId() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetId() {
		fail("Not yet implemented");
	}

}
