package cn.edu.ruc.hola.exec

import cn.edu.ruc.hola.config.Config
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.utils.Response
import cn.edu.ruc.hola.utils.ShuffleUtils.randomChoose

/**
 * exec with multi holder
 * for instance : one container holds 10 couch database
 */
class MultiExec(config: Config) : Exec(config) {

    private val DEFAULT_CNT = 10

    private fun getDBNames(cnt: Int): List<String> {
        val l: MutableList<String> = ArrayList()
        for (i in 0 until cnt) {
            l.add("fake$i")
        }
        return l
    }

    override fun partition(condition: String?): Response {
        val cutIds: List<String?> = randomChoose(sids(), queryConfig.m)
        val queries = query.queries(condition, cutIds, getDBNames(DEFAULT_CNT))

        println(queries!!.size)
        queries.forEach { x: Node? ->
            println(
                x
            )
        }
        return analyze(queries)
    }
}