package cn.edu.ruc.hola.benchmark.buddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class Interceptor {
    @RuntimeType
    public static Object intercept(@This Object self,
                                   @Origin Method method,
                                   @AllArguments Object[] args,
                                   @SuperMethod Method superMethod) throws Throwable {
        return superMethod.invoke(self, args);
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Class<?> type = new ByteBuddy()
                .subclass(Sample.class)
                .method(ElementMatchers.any()).intercept(MethodDelegation.to(Interceptor.class))
                .make()
                .load(Sample.class.getClassLoader())
                .getLoaded();

        String helloWorld = new ByteBuddy()
                .subclass(Object.class)
                .method(named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(Interceptor.class.getClassLoader())
                .getLoaded()
                .newInstance()
                .toString();
        System.out.println(helloWorld);  // Hello World!
    }
}