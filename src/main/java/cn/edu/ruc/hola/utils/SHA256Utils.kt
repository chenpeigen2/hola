package cn.edu.ruc.hola.utils

import org.apache.commons.codec.digest.DigestUtils
import java.util.logging.Logger

object SHA256Utils {
    private val logger = Logger.getLogger("SHA256Utils")

    fun getSHA256Str(str: String?): String {
        val encodeStr: String = DigestUtils.sha256Hex(str)
        logger.info(encodeStr)
        return encodeStr
    }

    fun verify(text: String, sha256: String): Boolean {
        val sha256Str = getSHA256Str(text)
        return sha256Str.equals(sha256, ignoreCase = true)
    }



}