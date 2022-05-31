package cn.edu.ruc.hola.config

/**
 * the system configs
 *  @param threshold     value 0 < x <= 1
 * @param time_interval time interval
 */
data class Config(var threshold: Float = -1f, val time_interval: Int = 1000, val M: Int = 10, val H: Int = 8)