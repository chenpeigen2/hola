package cn.edu.ruc.hola.service

import cn.edu.ruc.hola.entity.Node

interface Service {
    /**
     * @param condition query-condition
     * @param db_name   database name
     * @return the node-value
     */
    suspend fun query(condition: String, db_name: String): Node?
}