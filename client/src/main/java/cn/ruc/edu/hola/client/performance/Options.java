package cn.ruc.edu.hola.client.performance;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// https://juejin.cn/post/6844904047707684872
public interface Options {
    int WorkerPoolSize = 40;

    Lock lock = new ReentrantLock();
}
