package cn.edu.ruc.hola.benchmark;

import com.google.common.base.Stopwatch;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public interface Proxy<T> {

    default Object stopwatch(T t, Method m) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Object obj = proxy(t, m);
        stopwatch.stop();
        System.out.printf("执行时长：%d 秒. %n", stopwatch.elapsed().getSeconds());
        System.out.printf("执行时长：%d 豪秒.%n", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        System.out.printf("执行时长：%d 纳秒.%n", stopwatch.elapsed(TimeUnit.NANOSECONDS));
        return obj;
    }

    Object proxy(T t, Method m);
}
