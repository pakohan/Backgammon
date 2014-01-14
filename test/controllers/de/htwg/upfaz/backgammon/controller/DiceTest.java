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
		dice.rollTheDice();
		assertNotNull(dice.getValues()[0]);
	}

	@Test
	public void testHasTurnsLeft() {
		Dice p = new Dice();
		assertTrue(p.hasTurnsLeft());
		p.setNumberTurnsLeft(0);
		assertFalse(p.hasTurnsLeft());
	}

	@Test
	public void testGetDiceToDraw() {
		Dice p = new Dice();
		assertNotNull(p.getDiceToDraw(0));
	}

	@Test
	public void testGetDiceAt() {
		Dice p = new Dice();
		assertNotNull(p.getDiceAt(0));
	}

	@Test
	public void testMove() {
		Dice p = new Dice();
		assertTrue(p.move(p.getDiceAt(0)));
		
		p.setNumberTurnsLeft(0);
		assertFalse(p.move(p.getDiceAt(0)));
	}

	@Test
	public void testRenewJumpsEndPhase() {
		Dice p = new Dice();
		assertTrue(p.renewJumpsEndPhase(p.getDiceAt(0) - 1));
		
		p.setNumberTurnsLeft(0);
		assertFalse(p.renewJumpsEndPhase(p.getDiceAt(0) - 1));
	}

	@Test
	public void testCheckDistance() {
		Dice p = new Dice();
		assertTrue(p.checkDistance(p.getDiceAt(0)));
	}

	@Test
	public void testGetValues() {
		Dice p = new Dice();
		assertNotNull(p.getValues());
	}

	@Test
	public void testSetValues() {
		Byte[] toTest = new Byte[4];
		Dice p = new Dice();
		p.setValues(toTest);
		assertArrayEquals(p.getValues(), toTest);
	}

	@Test
	public void testGetValuesToDraw() {
		Dice p = new Dice();
		assertNotNull(p.getValuesToDraw());
	}

	@Test
	public void testSetValuesToDraw() {
		Byte[] toTest = new Byte[4];
		Dice p = new Dice();
		p.setValuesToDraw(toTest);
		assertArrayEquals(p.getValuesToDraw(), toTest);
	}

	@Test
	public void testGetNumberTurns() {
		Dice p = new Dice();
		assertNotNull(p.getNumberTurns());
	}

	@Test
	public void testGetNumberTurnsLeft() {
		Dice p = new Dice();
		assertNotNull(p.getNumberTurnsLeft());
	}

	@Test
	public void testSetNumberTurnsLeft() {
		Dice p = new Dice();
		int toTest = 5;
		p.setNumberTurnsLeft(toTest);
		assertEquals(p.getNumberTurnsLeft(), toTest);
	}

	@Test
	public void testSetNumberTurns() {
		Dice p = new Dice();
		int toTest = 5;
		p.setNumberTurns(toTest);
		assertEquals(p.getNumberTurns(), toTest);

	}

	@Test
	public void testGetId() {
		Dice p = new Dice();
		assertNotNull(p.getId());
	}

	@Test
	public void testSetId() {
		Dice p = new Dice();
		long x = 555;
		p.setId(x);
		assertEquals(p.getId(), x);
	}

}
