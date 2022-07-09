package cn.edu.ruc.hola.main

import cn.edu.ruc.hola.config.Config
import cn.edu.ruc.hola.exec.MultiExec
import cn.edu.ruc.hola.utils.LoggingUtils
import cn.edu.ruc.hola.utils.Response

object QueryWithMulti {
    private val logger = LoggingUtils.logger("QueryWithMulti")
    var RetryCnt = 5

    /**
     * the entry method , core method
     */
    fun queryWithTry(config: Config, condition: String): Response? {
        for (i in 0 until RetryCnt) {
            val res = query(config, condition)
            if (Response.SUCCESS_CODE == res["code"] as Int?) {
                logger.info("=========$i=========")
                return res
            }
        }
        return null
    }

    fun query(config: Config, condition: String): Response {
        val exec = MultiExec(config)
        return exec.partition(condition)!!
    }
}