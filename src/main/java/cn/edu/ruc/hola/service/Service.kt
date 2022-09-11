package cn.edu.ruc.hola.service

import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.utils.anno.PerformanceOnly

interface Service {
    /**
     * @param condition query-condition
     * @param db_name   database name
     * @return the node-value
     */
    suspend fun query(condition: String, db_name: String): Node?

    /**
     * performance test only
     */
    @PerformanceOnly
    fun performance(condition: String, db_name: String)
}