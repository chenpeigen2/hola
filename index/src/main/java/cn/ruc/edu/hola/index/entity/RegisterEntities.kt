package cn.ruc.edu.hola.index.entity

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.annotations.Expose

private val gson = GsonBuilder()
    .excludeFieldsWithoutExposeAnnotation()
    .create()

data class RegisterInfo(
    @Expose
    val serviceInfo: ServiceInfo,
    @Expose
    val struct: JsonElement
) {
    override fun toString(): String {
        return gson.toJson(this)
    }
}


data class ServiceInfo(
    @Expose
    val host: String,
    @Expose
    val port: Int,
    @Expose
    val path: String,
    @Expose(serialize = false)
    val serviceId: String,
    @Expose
    val serviceName: String
)
