package cn.ruc.edu.hola.client.config;

import okhttp3.logging.HttpLoggingInterceptor;

// https://juejin.cn/post/6844904047707684872
public interface Options {
    int WorkerPoolSize = 40;

    int Periodic_Millis = 50;

    HttpLoggingInterceptor.Level LEVEL = HttpLoggingInterceptor.Level.NONE;
}
