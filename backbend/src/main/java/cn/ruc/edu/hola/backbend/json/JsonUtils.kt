package cn.ruc.edu.hola.backbend.json

import cn.ruc.edu.hola.backbend.entity.Auth
import com.google.gson.Gson

object JsonUtils {

    private val json = Gson()

    fun String.toAuth(): Auth {
        return json.fromJson(this, Auth::class.java)
    }
}