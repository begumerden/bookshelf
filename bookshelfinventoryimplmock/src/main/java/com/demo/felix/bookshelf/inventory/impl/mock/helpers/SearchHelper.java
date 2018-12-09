package com.demo.felix.bookshelf.inventory.impl.mock.helpers;

/**
 * @author begum
 */
public class SearchHelper {

    public static boolean checkStringMatch(String attr, String critVal) {
        if (attr == null) {
            return false;
        }
        attr = attr.toLowerCase();
        critVal = critVal.toLowerCase();

        boolean startsWith = critVal.startsWith("%");
        boolean endsWith = critVal.endsWith("%");
        if (startsWith && endsWith) {
            if (critVal.length() == 1) {
                return true;
            } else {
                return attr.contains(critVal.substring(1, critVal.length() - 1));
            }
        } else if (startsWith) {
            return attr.endsWith(critVal.substring(1));
        } else if (endsWith) {
            return attr.startsWith(
                    critVal.substring(0, critVal.length() - 1));
        } else {
            return attr.equals(critVal);
        }
    }


    public static boolean checkIntegerGreater(int attr, String critVal) {
        int critValInt;
        try {
            critValInt = Integer.parseInt(critVal);
        } catch (NumberFormatException e) {
            return false;
        }
        if (attr >= critValInt) {
            return true;
        }
        return false;
    }

}
