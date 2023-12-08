package cn.ruc.edu.hola.backbend.register

import cn.ruc.edu.hola.backbend.entity.RegisterInfo
import cn.ruc.edu.hola.backbend.json.JsonUtils
import okhttp3.*
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

private object IndexServiceEntity {
    val scheme: String
        get() = "http"
    val host: String
        get() = "127.0.0.1"
    val port: Int
        get() = 2999
    val pathSegment: String
        get() = "register"
}

object Registers {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .build()

    private val headers = hashMapOf(
        "Accept" to "application/json",
        "Content-Type" to "application/json"
    )

    private fun RegisterInfo.toJsonString(): String {
        return JsonUtils.json.toJson(this)
    }

    fun doRegister(registerInfo: RegisterInfo) {

        val body = registerInfo.toJsonString().toRequestBody("application/json".toMediaType())

        val url = HttpUrl.Builder()
            .scheme(IndexServiceEntity.scheme)
            .host(IndexServiceEntity.host)
            .port(IndexServiceEntity.port)
            .addPathSegment(IndexServiceEntity.pathSegment)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(body)
            .headers(headers.toHeaders())
//            .cacheControl()
            .tag("doRegister")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                println(response.body?.string())
            }

        })
    }
}