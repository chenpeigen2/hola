package cn.edu.ruc.hola.utils

import org.apache.commons.codec.digest.DigestUtils

object SHA256Utils {

    fun getSHA256Str(str: String): String {
        return DigestUtils.sha256Hex(str)
    }

    fun verify(text: String, sha256: String): Boolean {
        val sha256Str = getSHA256Str(text)
        return sha256Str.equals(sha256, ignoreCase = true)
    }
}