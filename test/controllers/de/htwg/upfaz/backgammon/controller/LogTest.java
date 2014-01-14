package controllers.de.htwg.upfaz.backgammon.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public final class LogTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testVerbose() throws Exception {
        Log.verbose("TEST");
    }

    @Test
    public void testVerboseEx() throws Exception {
        Log.verbose(new Exception());
    }
}
