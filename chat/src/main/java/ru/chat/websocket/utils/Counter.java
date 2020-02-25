package ru.chat.websocket.utils;

import java.util.HashMap;
import java.util.Map;

public class Counter {

    private static Map <String, Integer> map = new HashMap<String, Integer>();

    public static void add (String hash) {
        try {
            int val = map.get(hash);

            map.put(hash, val + 1);
        }
        catch (NullPointerException e) {
            map.put(hash, 1);
        }
    }

    public static void remove (String hash) {
        try {
            int val = map.get(hash);

            map.put(hash, val - 1);
        }
        catch (NullPointerException e) {
            map.put(hash, 0);
        }
    }

    public static int get (String hash) {
        try {
            return map.get(hash);
        }
        catch (NullPointerException e) {
            return 0;
        }
    }
}
