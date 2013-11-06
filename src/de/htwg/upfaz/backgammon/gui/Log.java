package de.htwg.upfaz.backgammon.gui;

public final class Log {

    private Log() {}

    public static void verbose(final String s) {
        System.err.println(s);
    }

    public static void verbose(final Exception e) {
        e.printStackTrace();
    }
}
