package ru.skoch.jaycane;

import java.util.Random;

/**
 * Created by root on 25.07.16.
 */
public class CodeGenerator {

    static String characters = "asdfghjklqwertyuiopzxcvbnm";
    static int length=20;

    public static String generate(){
        Random rnd = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++){
            text[i] = characters.charAt(rnd.nextInt(characters.length()));
        }
        return new String(text);
    }

}
