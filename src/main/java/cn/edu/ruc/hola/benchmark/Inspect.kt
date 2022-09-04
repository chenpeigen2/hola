package cn.edu.ruc.hola.benchmark

import cn.edu.ruc.hola.utils.SHA256Utils

@NoCurrentUsage
data class Inspect(
    val execTimes: List<Long>,
    val realCredibility: Float,
    val theoreticalCredibility: List<Double>,
    val queryData: String,
) {
    val sha256: String
        get() {
            return SHA256Utils.getSHA256Str(queryData)
        }
    val avgExecTimes: Long
        get() {
            return execTimes.average().toLong()
        }
}
