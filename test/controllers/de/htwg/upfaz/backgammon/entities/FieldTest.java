package controllers.de.htwg.upfaz.backgammon.entities;

import static org.junit.Assert.assertNotNull;

import javax.swing.filechooser.FileSystemView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controllers.de.htwg.upfaz.backgammon.controller.Dice;
import junit.framework.TestCase;

public class FieldTest
        extends TestCase {

    private IField field;

    @Before
    public void setUp() throws Exception {
        field = new Field(0);
    }
    
    @After
	public void tearDown() throws Exception {
		field = null;
	}

    @Test
	public void testField() {
		assertNotNull(new Field());
	}
    @Test
    public void testgetFieldNr() {
        field.setFieldNr(1);
        assertEquals(1, field.getFieldNr());
        field.setFieldNr(0);
        assertEquals(0, field.getFieldNr());
    }
    @Test
    public void testgetNumberStones() {
        field.setNumberStones(1);
        assertEquals(1, field.getNumberStones());
        field.setNumberStones(0);
        assertEquals(0, field.getNumberStones());
    }
    
    @Test
    public void testGetStoneColor(){
    	assertNotNull(field.getStoneColor());
    }
    
    @Test
    public void testIsNotJumpable(){
    	field.setNumberStones(2);
    	field.setStoneColor(0);
    	assertTrue(field.isNotJumpable(1));
    	assertFalse(field.isNotJumpable(0));
    	field.setNumberStones(0);
    	assertTrue(field.isNotJumpable(1));
    	assertFalse(field.isNotJumpable(0));
    }
    
    @Test
    public void testSetAndGetId(){
    	((Field) field).setId(12345678910L);
    	assertEquals(((Field) field).getId(), 12345678910L);
    }
    
    @Test
    public void testToString(){
    	Field f = new Field(9);
    	f.setNumberStones(0);
    	assertEquals("   ", f.toString());
    	f.setNumberStones(3);
    	f.setStoneColor(0);
    	assertTrue(f.toString().contains("03w"));
    	f.setStoneColor(1);
    	assertTrue(f.toString().contains("03b"));
    	f.setNumberStones(11);
    	assertTrue(f.toString().contains("13b"));
    	f.setStoneColor(0);
    	assertTrue(f.toString().contains("13w"));
    }
}
