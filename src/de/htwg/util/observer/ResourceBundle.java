package de.htwg.util.observer;

import java.util.Locale;

public final class ResourceBundle {

    private ResourceBundle() {}
    public static String getString(String key) {
        return java.util.ResourceBundle.getBundle("region", new Locale("en", "us")).getString(key);
    }
}
