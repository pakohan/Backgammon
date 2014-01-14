package controllers.de.htwg.upfaz.backgammon.controller;

public final class Log {

    public static void verbose(final String s) {
        System.err.println(s);
    }

    public static void verbose(final Exception e) {
        e.printStackTrace();
    }
}
