package cn.edu.ruc.hola.benchmark

import com.google.common.base.Stopwatch
import java.util.concurrent.TimeUnit

object StopWatch {
    val stopwatch: Stopwatch = Stopwatch.createUnstarted()
    fun start() {
        stopwatch.start()
    }

    fun stop() {
        stopwatch.stop()
        print()
    }

    fun print() {
        System.out.printf("执行时长：%d 秒. %n", stopwatch.elapsed().seconds)
        System.out.printf("执行时长：%d 豪秒.%n", stopwatch.elapsed(TimeUnit.MILLISECONDS))
        System.out.printf("执行时长：%d 纳秒.%n", stopwatch.elapsed(TimeUnit.NANOSECONDS))
    }
}