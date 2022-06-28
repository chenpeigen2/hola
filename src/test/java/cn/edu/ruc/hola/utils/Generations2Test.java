package cn.edu.ruc.hola.utils;

import cn.edu.ruc.hola.benchmark.StopWatch;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Generations2Test {

    @Test
    void infPBA() {
    }

    @Test
    void optimize() {
    }

    @Test
    void testC() {
        long value = Generations2.C(80, 2);
        assertEquals(3160, value);
    }
}