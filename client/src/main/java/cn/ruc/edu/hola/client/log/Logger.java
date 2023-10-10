package cn.ruc.edu.hola.client.log;

import java.util.Arrays;

public class Logger {
    public static void info(String... args) {
        System.err.println(Arrays.toString(args));
    }
}
