package cn.edu.ruc.hola.config


/**
 * * private int m; // 本次查询查询节点数量
 * * private float λ; // 本次查询partition percentage >= thresh-hold (fixed value)
 * * private double θ; // 可信度
 */

data class QueryConfig(
    val m: Int,
    val λ: Float,
    val θ: Double
)
