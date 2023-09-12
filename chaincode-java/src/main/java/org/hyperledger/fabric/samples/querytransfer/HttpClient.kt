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
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
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

    fun executeHello(url: String, filter: String): Response {
        val body = filter.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .method("POST", body)
            .headers(headers.toHeaders())
            .tag("executeHello")
            .build()
        return client.newCall(request).execute()
    }

    fun getBaseUrl(config: SgxConfig): HttpUrl.Builder {
        return HttpUrl.Builder().scheme(config.scheme).host(config.host).port(config.port);
    }
}