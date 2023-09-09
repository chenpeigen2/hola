package cn.ruc.edu.hola.client.utils

object Timer {

    private const val ELAPSE_MINUTES = 15

    private var lastTime: Long = -1L

    fun shouldRefresh(): Boolean {
        val currentTime: Long = System.currentTimeMillis()
        if (lastTime == -1L) {
            lastTime = currentTime
        }
        if (currentTime - lastTime >= ELAPSE_MINUTES * 60 * 1000) {
            lastTime = currentTime
            return true
        }
        return false
    }

}