
package com.jiec.utils;

public class PhoneNumUtils {
    public static String toStarPhoneNumber(String number) {
        if (number.length() == 11) {
            number = number.substring(0, 2) + "****" + number.substring(7);
        } else if (number.length() > 4) {
            number = "****" + number.substring(4);
        }
        return number;
    }

    public static String standard(String str) {
        if (str != null && str.startsWith("+86")) {
            str = str.substring(3);
        }
        return str;
    }
}
