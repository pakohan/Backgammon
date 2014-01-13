package controllers.de.htwg.upfaz.backgammon.entities;

import org.junit.After;
import org.junit.Before;

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

    public void testgetFieldNr() {
        field.setFieldNr(1);
        assertEquals(1, field.getFieldNr());
        field.setFieldNr(0);
        assertEquals(0, field.getFieldNr());
    }

    public void testgetNumberStones() {
        field.setNumberStones(1);
        assertEquals(1, field.getNumberStones());
        field.setNumberStones(0);
        assertEquals(0, field.getNumberStones());
    }
}
