package cn.edu.ruc.hola.benchmark;

import cn.edu.ruc.hola.utils.BuddyUtils2;

public class HelloWorld {

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

    public static void main(String[] args) {
        var c = BuddyUtils2.getInstance(new Call(), Call.class);
        c.call1("peter");
        c.call2();
    }
}