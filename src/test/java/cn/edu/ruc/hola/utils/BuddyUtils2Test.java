package cn.edu.ruc.hola.utils;

import cn.edu.ruc.hola.benchmark.HelloWorld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuddyUtils2Test {

    public static class Call {
        public String call1(String ok) {
            System.out.println(" call 1 call hello " + ok);
            return "";
        }

        public String call2() {
            System.out.println(" call 2 ");
            return "hello world";
        }
    }

    @Test
    HelloWorld.Call getInstance() {
        var c = BuddyUtils2.getInstance(new HelloWorld.Call(), HelloWorld.Call.class);
        assertNotNull(c);
        return c;
    }

    @Test
    void call() {
        final var call = getInstance();
        call.call1("ok");
    }
}