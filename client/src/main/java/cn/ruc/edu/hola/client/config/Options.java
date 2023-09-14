package cn.ruc.edu.hola.client.config;

import okhttp3.logging.HttpLoggingInterceptor;

// https://juejin.cn/post/6844904047707684872
public interface Options {
    int WorkerPoolSize = 50;

    int Periodic_Millis = 100;

    HttpLoggingInterceptor.Level LEVEL = HttpLoggingInterceptor.Level.NONE;
}
