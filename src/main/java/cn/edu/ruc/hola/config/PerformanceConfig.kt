package cn.edu.ruc.hola.config

import cn.edu.ruc.hola.utils.anno.PerformanceOnly

/**
 * config only for performance test only
 */
@PerformanceOnly
object PerformanceConfig {

    const val STEP_1 = 1

    const val STEP_2 = 2

    const val STEP_3 = 4

    const val STEP = STEP_1

    /** one test only launch one**/
    const val fairy_1 = true

    /** indicate whether we use the sample-method or not */
    const val useSample = false

}