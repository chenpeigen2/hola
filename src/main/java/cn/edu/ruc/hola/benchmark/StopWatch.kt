package cn.edu.ruc.hola.benchmark

import com.google.common.base.Stopwatch
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

object StopWatch {
    val stopwatch: Stopwatch = Stopwatch.createUnstarted()
    fun start() {
        stopwatch.start()
    }

    fun stop() {
        stopwatch.stop()
        print()
        dump()
    }

    fun print() {
        System.out.printf("执行时长：%d 秒. %n", stopwatch.elapsed().seconds)
        System.out.printf("执行时长：%d 豪秒.%n", stopwatch.elapsed(TimeUnit.MILLISECONDS))
        System.out.printf("执行时长：%d 纳秒.%n", stopwatch.elapsed(TimeUnit.NANOSECONDS))
    }

    private fun dump() {
        appendFile("执行时长: ${stopwatch.elapsed(TimeUnit.MILLISECONDS)} 纳秒", "./output.txt")
    }

    private fun appendFile(text: String, destFile: String) {
        val f = File(destFile)
        if (!f.exists()) {
            f.createNewFile()
        }
        f.appendText(text, Charset.defaultCharset())
    }
}