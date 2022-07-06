package cn.edu.ruc.hola.entity

import cn.edu.ruc.hola.utils.SHA256Utils

data class Results(val data: String = "", val θ: Double = -1.0) {
    val sha256: String
        get() {
            return SHA256Utils.getSHA256Str(data)
        }
}