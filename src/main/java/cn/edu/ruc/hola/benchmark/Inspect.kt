package cn.edu.ruc.hola.benchmark

data class Inspect(
    val execTimes: List<Long>,
    val realCredibility: Float,
    val theoreticalCredibility: List<Float>,
    val queryData: String
) {
    val avgExecTimes: Long
        get() {
            return execTimes.average().toLong()
        }
}
