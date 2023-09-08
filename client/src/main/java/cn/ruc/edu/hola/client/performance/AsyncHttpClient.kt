package cn.ruc.edu.hola.client.performance

import cn.ruc.edu.hola.client.Query
import cn.ruc.edu.hola.client.impl.SgxConfig
import io.vertx.ext.web.impl.LRUCache
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
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
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
            .tag("complex#chaincode")
            .build()
        return client.newCall(request).execute()
    }

    private val cache = LRUCache<String, String>(20)

    fun executeQuery(queryList: List<Query>, config: SgxConfig) = runBlocking {
        cache.clear() // remove previous cache
        queryList.forEach {
            it.resp = async(Dispatchers.Default) {
                var text = cache[it.query]
                if (text == null) {
                    text = executeComplex(config, it.query).use { response ->
                        response.body?.string()
                    }
                    cache[it.query] = text
                }
                text
            }.await()
        }
    }

}