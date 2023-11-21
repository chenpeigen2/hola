package cn.ruc.edu.hola.backbend.config;

import okhttp3.logging.HttpLoggingInterceptor;

public interface Options {
    boolean DEBUG = false;

    HttpLoggingInterceptor.Level LEVEL = HttpLoggingInterceptor.Level.NONE;

    String Port = "port";
}
