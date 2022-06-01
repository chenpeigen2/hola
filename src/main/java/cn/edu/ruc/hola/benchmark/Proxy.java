package cn.edu.ruc.hola.benchmark;

import java.lang.reflect.Method;

/**
 * cooperate with the BuddyUtils
 *
 * @param <T> type
 */
@Deprecated
public interface Proxy<T> {

    StopWatch w = StopWatch.INSTANCE;

    @Deprecated
    default Object stopwatch(T t, Method m, Object[] args) {
        w.getStopwatch().start();
        Object obj = proxy(t, m, args);
        w.getStopwatch().stop();
        w.print();
        return obj;
    }

    @Deprecated
    Object proxy(T t, Method m, Object[] args);
}
