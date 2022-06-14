package cn.edu.ruc.hola.service

import cn.edu.ruc.hola.entity.Node

interface Service {
    /**
     * for mysql
     *
     * @param condition query-condition
     * @return the node-value
     */
    fun query(condition: String?): Node?

    /**
     * for couchdb
     *
     * @param condition query-condition
     * @param db_name   database name
     * @return the node-value
     */
    fun query(condition: String?, db_name: String?): Node?

    /**
     * @param condition query-condition
     * @param db_names  database names
     * @return list of node-value support by one instance
     */
    fun queries(condition: String?, db_names: List<String?>?): List<Node?>?
}