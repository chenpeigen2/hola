package cn.edu.ruc.hola.utils;

import cn.edu.ruc.hola.benchmark.StopWatch;
import com.google.common.base.Stopwatch;
import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public final class BuddyUtils2 {
    private static final Logger logger = Logger.getLogger("RBuddyUtils");

    private BuddyUtils2() {

    }

    private static final Stopwatch stopwatch = StopWatch.INSTANCE.getStopwatch();

    public static class MethodInterceptor<T> {
        private final T t;

        public MethodInterceptor(T t) {
            this.t = t;
        }

        @RuntimeType
        public Object interceptor(@This Object instance, @Origin Method method, @AllArguments Object[] args) throws InvocationTargetException, IllegalAccessException {
            logger.info("$$$ proxy at method : " + method.getName() + ", at class: " + t.getClass().toGenericString());
            stopwatch.start();
            Object obj = method.invoke(t, args);
            stopwatch.stop();
            StopWatch.INSTANCE.print();
            return obj;
        }
    }

    /**
     * @param cls class can't be final
     * @return the proxy class
     */
    public static <T> Class<? extends T> createClass(Class<T> cls, T t) {
        var make = new ByteBuddy()
                .subclass(cls)
                .method(ElementMatchers.isDeclaredBy(cls))
                .intercept(MethodDelegation.to(new BuddyUtils2.MethodInterceptor<>(t)))
                .make();
        DynamicType.Loaded<T> proxy = make.load(BuddyUtils2.class.getClassLoader());
        return proxy.getLoaded();
    }

    /**
     * get instance object
     **/
    @SneakyThrows
    public static <T> T getInstance(T t, Class<T> cls, Object... initargs) {
        var c = createClass(cls, t);
        return c.getDeclaredConstructor().newInstance(initargs);
    }
}
