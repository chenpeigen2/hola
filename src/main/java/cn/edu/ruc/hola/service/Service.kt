package cn.edu.ruc.hola.service

interface Service {
    /**
     * @param condition query-condition
     * @param db_name   database name
     * @return the node-value
     */
    suspend fun query(condition: String, db_name: String)

    /**
     * performance test only
     */
    suspend fun performance(condition: String, db_name: String)
}