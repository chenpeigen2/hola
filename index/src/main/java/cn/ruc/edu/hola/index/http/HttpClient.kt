package cn.ruc.edu.hola.index.http

import okhttp3.Headers.Companion.toHeaders
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

object HttpClient {

    private val headers = hashMapOf(
        "Accept" to "application/json",
        "Content-Type" to "application/json"
    )

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .build()

    fun executePing(host: String, port: Int, path: String): Response {
        val url = HttpUrl.Builder().scheme("http")
            .host(host)
            .port(port)
            .addPathSegment(path)
            .build()
        val request = Request.Builder()
            .url(url)
            .headers(headers.toHeaders())
            .get()
            .tag("executeHello")
            .build()
        return client.newCall(request).execute()
    }
}