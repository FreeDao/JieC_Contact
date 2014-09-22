
package com.jiec.contact.utils;

public class LogUtil {

    private static boolean isDebug = false;

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    public static void d(String msg) {
        if (isDebug) {
            System.out.println(msg);
        }
    }

    public static void e(String msg) {
        System.out.println(msg);
    }

}
