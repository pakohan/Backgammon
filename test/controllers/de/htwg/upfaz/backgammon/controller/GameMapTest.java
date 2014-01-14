package controllers.de.htwg.upfaz.backgammon.controller;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import controllers.de.htwg.upfaz.backgammon.entities.Field;


public class GameMapTest {

	private GameMap gm;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		gm = new GameMap(new Players(), new Dice());
	}

	@After
	public void tearDown() throws Exception {
		gm = null;
	}

	@Test
	public void testGameMapIPlayerDice() {
		assertNotNull(new GameMap(new Players(), new Dice()));
	}

	@Test
	public void testGameMap() {
		assertNotNull(new GameMap());
	}

	@Test
	public void testMoveStone() {
		int start = 5;
		int tar = 10;
		gm.getField(start).setNumberStones(3);
		int stonesTarBefore = gm.getField(tar).getNumberStones();
		int stonesStartBefore = gm.getField(start).getNumberStones();
		gm.moveStone(start, tar);
		assertEquals(stonesTarBefore + 1, gm.getField(tar).getNumberStones());
		assertEquals(stonesStartBefore - 1, gm.getField(start)
				.getNumberStones());
		assertThat(-1, not(gm.getField(start).getStoneColor()));
		gm.getField(start).setNumberStones(1);
		gm.moveStone(start, tar);
		assertEquals(-1, gm.getField(start).getStoneColor());

	}

	@Test
	public void testEatStone() {
		int start = 5;
		int tar = 10;
	//	int stonesTarBefore = gm.getField(tar).getNumberStones();
		int stonesStartBefore = gm.getField(start).getNumberStones();

		gm.eatStone(start, tar);

		assertEquals(1, gm.getField(tar).getNumberStones());
		assertEquals(stonesStartBefore - 1, gm.getField(start)
				.getNumberStones());
		assertThat(-1, not(gm.getField(start).getStoneColor()));
	}

	@Test
	public void testGetField() {
		assertNotNull(gm.getField(0));
	}

	@Test
	public void testGetFields() {
		Field[] f = new Field[11];
		gm.setFields(f);
		assertArrayEquals(gm.getFields(), f);
	}

	@Test
	public void testSetFields() {
		Field[] f = new Field[4];
		gm.setFields(f);
		assertArrayEquals(gm.getFields(), f);
	}

	@Test
	public void testGetCurrentPlayer() {
		assertNotNull(gm.getCurrentPlayer());
	}

	@Test
	public void testIsWhiteEaten() {
		GameMap g = new GameMap(new Players(), new Dice());
		assertFalse(g.isWhiteEaten());
	}

	@Test
	public void testIsBlackEaten() {
		GameMap g = new GameMap(new Players(), new Dice());
		assertFalse(g.isBlackEaten());
	}

	@Test
	public void testGetUuid() {
		assertNotNull(gm.getUuid());
	}

	@Test
	public void testIsNew() {
		gm.set_rev("test");
		assertFalse(gm.isNew());
	}

	@Test
	public void testSetUuid() {
		UUID x = new UUID(123, 123);
		gm.setUuid(x);
		assertEquals(x, gm.getUuid());
	}

	@Test
	public void testGet_rev() {
		gm.set_rev("xxx");
		assertNotNull(gm.get_rev());
	}

	@Test
	public void testSet_rev() {
		gm.set_rev("test");
		assertEquals(gm.get_rev(), "test");
	}

	@Test
	public void testCheckForWinner() {
		GameMap x = new GameMap(new Players(), new Dice());
		assertFalse(x.checkForWinner());
	}

	@Test
	public void testGetRevision() {
		gm.setRevision("xxx");
		assertNotNull(gm.getRevision());
	}

	@Test
	public void testSetRevision() {
		gm.setRevision("xxx");
		assertEquals(gm.getRevision(), "xxx");
	}

	@Test
	public void testGetId() {
		assertNotNull(gm.getId());
	}

	@Test
	public void testSetId() {
		gm.setId("aaa");
		assertEquals(gm.getId(), "aaa");
	}

	@Test
	public void testGetDice() {
		assertNotNull(gm.getDice());
	}

	@Test
	public void testSetDice() {
		Dice x = new Dice();
		gm.setDice(x);
		assertEquals(gm.getDice(), x);
	}

	@Test
	public void testGetFirstClick() {
		assertNotNull(gm.getFirstClick());
	}

	@Test
	public void testSetFirstClick() {
		gm.setFirstClick(1);
		assertEquals(gm.getFirstClick(), 1);
	}

	@Test
	public void testGetSecondClick() {
		assertNotNull(gm.getSecondClick());
	}

	@Test
	public void testSetSecondClick() {
		gm.setSecondClick(5);
		assertEquals(gm.getSecondClick(), 5);
	}

	@Test
	public void testGetWinner() {
		assertNotNull(gm.getWinner());
	}

	@Test
	public void testSetWinner() {
		GameMap xx = new GameMap(new Players(), new Dice());
		xx.setWinner(10);
		assertEquals(10, xx.getWinner());
	}

	@Test
	public void testIsEndPhase() {
		GameMap g = new GameMap(new Players(), new Dice());
		assertFalse(g.isEndPhase());
	}

	@Test
	public void testSetEndPhase() {
		GameMap g = new GameMap(new Players(), new Dice());
		g.setEndPhase(true);
		assertTrue(g.isEndPhase());
	}

	@Test
	public void testGetPlayers() {
		assertNotNull(gm.getPlayers());
	}

	@Test
	public void testToString() {
		assertNotNull(gm.toString());
	}
}
