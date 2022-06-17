package cn.edu.ruc.hola.config

/**
 * host class with the given sid(service id) and then host the database config
 *
 * it may have multi
 */
data class Host(val sid: String, val host: String, val port: Int)