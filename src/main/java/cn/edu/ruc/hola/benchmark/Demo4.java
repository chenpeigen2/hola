package cn.edu.ruc.hola.benchmark;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType.Loaded;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;

public class Demo4 {

    public static class Call {
        public String call1(String ok) {
            System.out.println(" call 1 call fucking ");
            return "";
        }

        public void call2() {
            System.out.println(" call 2 ");
        }
    }


    public static void main(String[] args) throws Exception {
        Call proxy = createByteBuddyDynamicProxy();
        proxy.call1("");
    }

    private static Call createByteBuddyDynamicProxy() throws Exception {
        Loaded<Call> proxy = new ByteBuddy().subclass(Call.class)
                .method(ElementMatchers.isDeclaredBy(Call.class))
                .intercept(MethodDelegation.to(new MethodInterceptor(new Call())))
                .make().load(Demo4.class.getClassLoader());

        return proxy.getLoaded().getDeclaredConstructor().newInstance();
    }

    public static class MethodInterceptor {
        public Call call;

        public MethodInterceptor(Call call) {
            this.call = call;
        }

        @RuntimeType
        public Object interceptor(@This Object instance, @Origin Method method, @AllArguments Object[] args) throws Exception {
            System.out.println("bytebuddy method call before  ");
            Object result = method.invoke(this.call, args);
            System.out.println("bytebuddy method call after   ");
            return result;
        }
    }
}
