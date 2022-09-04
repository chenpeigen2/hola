package cn.edu.ruc.hola.benchmark

import cn.edu.ruc.hola.utils.anno.NoCurrentUsage
import com.google.common.base.Stopwatch
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

@NoCurrentUsage
object StopWatch {
    val stopwatch: Stopwatch = Stopwatch.createUnstarted()
    fun start() {
        stopwatch.reset()
        stopwatch.start()
    }

    fun stop(): Long {
        stopwatch.stop()
        return stopwatch.elapsed(TimeUnit.MILLISECONDS)
    }

    fun print() {
        System.out.printf("执行时长：%d 秒. %n", stopwatch.elapsed().seconds)
        System.out.printf("执行时长：%d 豪秒.%n", stopwatch.elapsed(TimeUnit.MILLISECONDS))
        System.out.printf("执行时长：%d 纳秒.%n", stopwatch.elapsed(TimeUnit.NANOSECONDS))
    }

    private fun dump() {
        appendFile("${stopwatch.elapsed(TimeUnit.MILLISECONDS)}\n")
    }

    private fun appendFile(text: String, destFile: String = "./log/output.txt") {
        val f = File(destFile)
        if (!f.exists()) {
            f.createNewFile()
        }
        f.appendText(text, Charset.defaultCharset())
    }
}