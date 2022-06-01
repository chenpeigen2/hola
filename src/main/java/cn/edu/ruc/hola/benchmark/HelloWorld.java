package cn.edu.ruc.hola.benchmark;

import cn.edu.ruc.hola.utils.RBuddyUtils;

public class HelloWorld {

    public static class Call {
        public String call1(String ok) {
            System.out.println(" call 1 call fucking " + ok);
            return "";
        }

        public String call2() {
            System.out.println(" call 2 ");
            return "mother fucking";
        }
    }

    public static void main(String[] args) {
        var c = RBuddyUtils.getInstance(new Call(), Call.class);
        c.call1("1123");
        c.call2();
    }
}