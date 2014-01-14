package controllers.de.htwg.upfaz.backgammon.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public final class LogTest {
    
	private Log l;
	@Before
    public void setUp() throws Exception {
    	l = new Log();
    }

    @After
    public void tearDown() throws Exception {
    	l = null;
    }

    @Test
    public void testVerbose() throws Exception {
        l.verbose("TEST");
    }

    @Test
    public void testVerboseEx() throws Exception {
        l.verbose(new Exception());
    }
}
