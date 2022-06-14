package cn.edu.ruc.hola.utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ShuffleUtilsTest {

    @Test
    fun randomChoose() {
        val l = mutableListOf<String>()
        l.apply {
            add("a")
            add("b")
            add("c")
            add("d")
            add("e")
            add("f")
            add("g")
        }
        val ans = ShuffleUtils.randomChoose(l, 3)
        print(ans)
    }
}