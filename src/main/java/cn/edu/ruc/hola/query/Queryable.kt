package cn.edu.ruc.hola.query

import cn.edu.ruc.hola.entity.Node

interface Queryable {
    fun query(condition: String?, sids: List<String?>?): List<Node?>?

    fun query(condition: String?, sids: List<String?>?, db_name: String?): List<Node?>?

    fun queries(condition: String?, sids: List<String?>?, db_names: List<String?>?): List<Node?>?
}