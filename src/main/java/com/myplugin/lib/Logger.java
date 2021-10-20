package com.myplugin.lib;

import com.myplugin.SpigotDragonZ;

import static com.myplugin.SpigotDragonZ.ofString;

public class Logger {

    private static final String prefix = "&6Dragon&eSpigot&6Z ";

    public static void debug(final String message) {
        if (SpigotDragonZ.DEBUG) {
            System.out.println(ofString(prefix + "&b:: &2DEBUG &b:: &a" + message));
        }
    }

    public static void log(final String message) {
        System.out.println(ofString(prefix + "&b:: &3INFO &b:: &b" + message));
    }

    public static void warn(final String message) {
        System.out.println(ofString(prefix + "&4:: &eWARN &4:: &c" + message));
    }

    public static void error(final String message) {
        System.out.println(ofString(prefix + "&4:: &cERROR &4:: &c" + message));
    }
}
