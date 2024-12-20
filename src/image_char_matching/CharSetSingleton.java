package image_char_matching;

import java.util.TreeMap;

public class CharSetSingleton {
    private TreeMap<Character, Integer> map;
    private static CharSetSingleton singleton;
    private CharSetSingleton() {map = new TreeMap<>();}
    public static CharSetSingleton getInstance() {
        if(singleton != null) {
            return singleton;
        }
        singleton = new CharSetSingleton();
        return singleton;
    }
    public char[] getChars(){
        char[] chars = new char[map.size()];
        int index = 0;
        for(Character key : map.keySet()) {
            chars[index] = key;
        }
        return chars;
    }
}
