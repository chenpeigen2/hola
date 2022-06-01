package cn.edu.ruc.hola.utils;

import cn.edu.ruc.hola.benchmark.Proxy;
import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * for test the performance is lower than the RBuddyUtils , do not use it
 */
@Deprecated
public class BuddyUtils {

    public static class MethodInterceptor<T> {
        private final T t;

        private final Proxy<T> consumer;

        public MethodInterceptor(T t, Proxy<T> consumer) {
            this.t = t;
            this.consumer = consumer;
        }

        @RuntimeType
        public Object interceptor(@This Object instance, @Origin Method method, @AllArguments Object[] args) throws InvocationTargetException, IllegalAccessException {
            return consumer.stopwatch(t, method, args);
        }
    }

    /**
     * @param cls class can't be final
     * @return the proxy class
     */
    public static <T> Class<? extends T> createClass(Class<T> cls, T t, Proxy<T> consumer) throws IOException {
        var make = new ByteBuddy()
                .subclass(cls)
                .method(ElementMatchers.isDeclaredBy(cls))
                .intercept(MethodDelegation.to(new MethodInterceptor<T>(t, consumer)))
                .make();
        DynamicType.Loaded<T> proxy = make.load(BuddyUtils.class.getClassLoader());
        return proxy.getLoaded();
    }

    /**
     * get instance object
     **/
    @SneakyThrows
    public static <T> T getInstance(T t, Class<T> cls, Proxy<T> consumer, Object... initargs) {
        var c = createClass(cls, t, consumer);
        return c.getDeclaredConstructor().newInstance(initargs);
    }
}
