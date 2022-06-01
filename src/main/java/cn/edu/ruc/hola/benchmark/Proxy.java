package cn.edu.ruc.hola.benchmark;

import com.google.common.base.Stopwatch;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public interface Proxy<T> {
    Stopwatch stopwatch = Stopwatch.createUnstarted();

    default Object stopwatch(T t, Method m, Object[] args) {
        stopwatch.start();
        Object obj = proxy(t, m, args);
        stopwatch.stop();
        print();
        return obj;
    }

    static void print() {
        System.out.printf("执行时长：%d 秒. %n", stopwatch.elapsed().getSeconds());
        System.out.printf("执行时长：%d 豪秒.%n", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        System.out.printf("执行时长：%d 纳秒.%n", stopwatch.elapsed(TimeUnit.NANOSECONDS));
    }

    Object proxy(T t, Method m, Object[] args);
}
