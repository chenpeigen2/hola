package cn.edu.ruc.hola.config

import cn.edu.ruc.hola.utils.Generations2

/**
 * the system configs
 *  @param threshold     value 0 < x <= 1
 * @param time_interval time interval
 */
data class Config(var threshold: Float = -1f, val time_interval: Int = 1000, val M: Int = 10, val H: Int = 8) {

    val queryConfig: QueryConfig
        get() {
            return Generations2.optimize(threshold, M, H)
        }
}