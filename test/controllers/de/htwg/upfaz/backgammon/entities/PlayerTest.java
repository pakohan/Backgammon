package controllers.de.htwg.upfaz.backgammon.entities;

import org.junit.After;
import org.junit.Before;

import controllers.de.htwg.upfaz.backgammon.controller.Dice;
import junit.framework.TestCase;

/**
 * Class Player
 */
public class PlayerTest
        extends TestCase {

    private IPlayer player;
  
    @Before
	public void setUp() throws Exception {
        player = new Player(0);
	}
    
	@After
	public void tearDown() throws Exception {
		player = null;
	}

 
    
    public void testgetColor() {
    	Player x = new Player(1);
        assertEquals(1, x.getColor());
    }
}
