package cn.edu.ruc.hola.main

import cn.edu.ruc.hola.benchmark.StopWatch
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

internal class AppKtTest {

    @BeforeEach
    fun beforeTest() {
        StopWatch.start()
    }

    @Test
    fun test01() {
        println(222)
    }


    @RepeatedTest(10)
    fun test02() {
        println(1)
    }

    @AfterEach
    fun afterTest() {
        StopWatch.stop()
    }
}