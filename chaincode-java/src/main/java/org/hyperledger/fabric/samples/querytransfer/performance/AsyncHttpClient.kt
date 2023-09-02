package org.hyperledger.fabric.samples.querytransfer.performance

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.hyperledger.fabric.samples.querytransfer.HttpClient
import org.hyperledger.fabric.samples.querytransfer.Query
import org.hyperledger.fabric.samples.querytransfer.SgxConfig

object AsyncHttpClient {
    suspend fun executeComplex(config: SgxConfig, query: String): Response {
        val body = query.toRequestBody("application/json".toMediaType())
        val url = HttpClient.getBaseUrl(config)
            .addPathSegment("_complex")
            .build()
        val request = Request.Builder()
            .url(url)
            .method("POST", body)
            .headers(HttpClient.headers.toHeaders())
//            .cacheControl()
            .tag("complex#chaincode")
            .build()
        return HttpClient.client.newCall(request).execute()
    }


    fun executeQuery(queryList: List<Query>, config: SgxConfig) = runBlocking {
        queryList.forEach {
            it.resp = async(Dispatchers.Default) {
                executeComplex(config, it.query).use { response ->
                    response.body?.string()
                }
            }.await()
        }
    }

}