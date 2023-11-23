package cn.ruc.edu.hola.backbend.entity

import cn.ruc.edu.hola.backbend.json.JsonUtils

data class Filter(val uuid: String, val filter: String)

data class Auth(val key: String)


fun String.toAuth(): Auth {
    return JsonUtils.json.fromJson(this, Auth::class.java)
}

fun String.toFilter(): Filter {
    return JsonUtils.json.fromJson(this, Filter::class.java)
}