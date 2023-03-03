package com.example.rqchallenge.util;

/**
 * Utility class to help with functionality in the services
 * 
 * @author sedake
 *
 */
public class Util {
    public static String fill(String URL, String param) {
        return URL.replaceFirst("\\{\\}", param);
    }
}
