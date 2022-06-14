package cn.edu.ruc.hola.utils

object ShuffleUtils {
    fun randomChoose(ids: List<String>, cnt: Int): List<String> {
        return ids.shuffled().subList(0, cnt)
    }

}