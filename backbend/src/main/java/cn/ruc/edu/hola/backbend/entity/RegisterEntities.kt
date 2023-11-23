package cn.ruc.edu.hola.backbend.entity

import com.google.gson.JsonElement

data class RegisterInfo(
    val serviceInfo: ServiceInfo,
    val struct: JsonElement
)


data class ServiceInfo(
    val host: String,
    val port: Int,
    val path: String,
    val serviceId: String,
    val serviceName: String
)
