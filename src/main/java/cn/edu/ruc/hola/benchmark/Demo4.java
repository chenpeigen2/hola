package cn.edu.ruc.hola.benchmark;

import cn.edu.ruc.hola.utils.BuddyUtils;
import cn.edu.ruc.hola.utils.RBuddyUtils;
import com.google.common.base.Stopwatch;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class Demo4 {

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
        var c = BuddyUtils.getInstance(new Call(), Call.class, new Proxy<Call>() {
            @Override
            public Object proxy(Call call, Method m, Object[] arg) {
                Object obj = null;
                if (m.getName().equals("call1")) {
                    System.out.println(arg.length);
                    System.out.println("fucking============");
                    call.call1((String) arg[0]);
                    System.out.println("fuckingEnding============");
                    return null;
                }
                if (m.getName().equals("call2")) {
                    return call.call2();
                }
                return obj;
            }
        });

        Stopwatch stopwatch = Stopwatch.createStarted();
        Call fucking = new Call();
        fucking.call1("hhhhhhhahahahaha");
        stopwatch.stop();
        // 执行时间（单位：秒）
        System.out.printf("执行时长：%d 秒. %n", stopwatch.elapsed().getSeconds()); // %n 为换行
        // 执行时间（单位：毫秒）
        System.out.printf("执行时长：%d 豪秒.%n", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        // 执行时间（单位：纳秒）
        System.out.printf("执行时长：%d 纳秒.%n", stopwatch.elapsed(TimeUnit.NANOSECONDS));
        // 427651 438832 395661


        var c1 = RBuddyUtils.getInstance(new Call(), Call.class);
//        c.call1("1123");

        c1.call1("1123");
//c1.call1("1123");
//
        var c2 = new Call();
        Proxy.stopwatch.start();
        c2.call1("1123");
        Proxy.stopwatch.stop();
        Proxy.print();



    }
}