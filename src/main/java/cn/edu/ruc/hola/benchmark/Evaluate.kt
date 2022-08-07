package cn.edu.ruc.hola.benchmark

import cn.edu.ruc.hola.benchmark.Bench.readCredibility
import cn.edu.ruc.hola.benchmark.Bench.saveResult
import cn.edu.ruc.hola.entity.Response
import cn.edu.ruc.hola.utils.QuickQueue
import com.google.gson.Gson
import java.io.File


object Evaluate {
    private val gson = Gson()
    private val f = File("./jmeter/jmx/data.txt")

    fun readCredibility(): Float {
        val q = QuickQueue()
        var cnt = 0;
        f.forEachLine {
            cnt++
            q.saveResult(gson.fromJson(it, Response::class.java))
        }
        return q.readCredibility(cnt)
    }

}

fun main() {
    val real = Evaluate.readCredibility()
    println(real)
}

