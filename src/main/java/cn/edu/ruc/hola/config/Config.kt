package cn.edu.ruc.hola.config

import cn.edu.ruc.hola.utils.Generations2

/**
 * the system configs
 *  @param threshold     value 0 < x <= 1
 * @param time_interval time interval
 */
data class Config(
    var threshold: Float = -1f,
    val time_interval: Int = 1000,
    val M: Int,
    val H: Int,
    val expected: Float = 0.9f
) {

    val queryConfig: QueryConfig
        get() {
            return Generations2.select(threshold, M, H, expected)
        }
}