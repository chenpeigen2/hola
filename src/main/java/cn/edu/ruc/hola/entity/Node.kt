package cn.edu.ruc.hola.entity

import cn.edu.ruc.hola.utils.SHA256Utils.getSHA256Str

/**
 * query condition-value holder
 *
 * @param condition : condition value the user pass
 * @param value     : with condition
 */
data class Node(val condition: String, val value: String) {
    val shaValue: String
        get() {
            return getSHA256Str(value)
        }

    val shaCondition: String
        get() {
            return getSHA256Str(condition)
        }

    val shaData: String
        get() {
            return getSHA256Str(toString())
        }
}
