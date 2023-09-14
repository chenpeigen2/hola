package org.hyperledger.fabric.samples.querytransfer

import okhttp3.Headers.Companion.toHeaders
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

object HttpClient {

    val headers = hashMapOf(
        "Accept" to "application/json",
        "Content-Type" to "application/json"
    )

    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
        .build()


    fun executeComplex(config: SgxConfig, query: String): Response {
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

    fun executeHello(): Response {
        val url = HttpUrl.Builder().scheme("http")
            .host("172.17.0.1")
            .port(8000)
            .build()
        val request = Request.Builder()
            .url(url)
            .get()
            .tag("executeHello")
            .build()
        return client.newCall(request).execute()

    }

    fun getBaseUrl(config: SgxConfig): HttpUrl.Builder {
        return HttpUrl.Builder().scheme(config.scheme).host(config.host).port(config.port);
    }
}