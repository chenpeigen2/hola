package cn.edu.ruc.hola.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SHA256UtilsTest {

    @Test
    fun getSHA256Str() {
        val str = SHA256Utils.getSHA256Str("hello world")
        val expected = """
            b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9
        """.trimIndent()
        assertEquals(expected, str)
    }

    @Test
    fun verify() {
        assertTrue(
            SHA256Utils.verify(
                "hello world",
                "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9"
            )
        )
    }
}