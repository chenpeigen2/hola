package cn.ruc.edu.hola.client.impl

import cn.ruc.edu.hola.client.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Headers.Companion.toHeaders
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

object AsyncHttpClient {

    private val headers = hashMapOf(
        "Accept" to "application/json",
        "Content-Type" to "application/json"
    )

    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
        .build()


    private fun getBaseUrl(config: SgxConfig): HttpUrl.Builder {
        return HttpUrl.Builder().scheme(config.scheme).host(config.host).port(config.port);
    }

    private suspend fun executeComplex(config: SgxConfig, query: String): Response {
        val body = query.toRequestBody("application/json".toMediaType())
        val url = getBaseUrl(config)
            .addPathSegment("_complex")
            .build()
        val request = Request.Builder()
            .url(url)
            .method("POST", body)
            .headers(headers.toHeaders())
//            .cacheControl()
            .tag("complex#client")
            .build()
        return client.newCall(request).execute()
    }

    fun executeQuery(queryList: List<Query>, config: SgxConfig) = runBlocking {
        queryList.forEach {
            it.resp = async(Dispatchers.IO) {
                executeComplex(config, it.query).use { response ->
                    response.body?.string()
                }
            }.await()
        }
    }
}