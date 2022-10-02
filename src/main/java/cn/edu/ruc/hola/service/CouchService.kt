package cn.edu.ruc.hola.service

import cn.deu.ruc.sample.QuerySq
import cn.edu.ruc.hola.config.PerformanceConfig
import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.utils.OkHttpResponseFuture
import cn.edu.ruc.hola.utils.anno.Core
import cn.edu.ruc.hola.utils.anno.PerformanceOnly
import cn.edu.ruc.hola.utils.perform.SuspendQuertSq

class CouchService(sid: String) : AbsService(sid) {

    private val dbExtension: CouchDbExtension

    private val gson = ServerHelper.gson

    init {
        val h = getHost(sid) ?: throw RuntimeException("host is null")
        dbExtension = CouchDbExtension(h)
    }

    @Core
    override suspend fun query(condition: String, db_name: String): Node? {
        return dbExtension.query(condition, db_name)
    }

    @Core
    override fun queryAsync(condition: String, db_name: String): OkHttpResponseFuture {
        return dbExtension.queryAsync(condition, db_name)
    }

    @PerformanceOnly
    override suspend fun performance(condition: String?, db_name: String) {
        val str = if (PerformanceConfig.useSample) {
            QuerySq.query()
        } else {
            SuspendQuertSq.query()
        }

//        if (PerformanceConfig.STEP == PerformanceConfig.STEP_1) {
        println(str)
//        }

//        if (PerformanceConfig.STEP == PerformanceConfig.STEP_2) {
//            str.let {
//                val filterDocs: FilterDocs =
//                    gson.fromJson(
//                        it,
//                        FilterDocs::class.java
//                    )
//
//                val data = buildString {
//                    filterDocs.docs?.forEach { doc ->
//                        append(doc.data.toString())
//                    }
//                }
//
//                val node = Node(condition, data)
//
//                println(node)
//            }
//        }
//
//        if (PerformanceConfig.STEP == PerformanceConfig.STEP_3) {
//
//        }

        // here
    }
}