
package com.jiec.utils;

public class PhoneNumUtils {
    public static String toStarPhoneNumber(String number) {
        if (number.length() == 11) {
            number = number.substring(0, 2) + "****" + number.substring(7);
        } else if (number.length() > 1) {
            number = "****" + number.substring(4);
        }
        return number;
    }
}
