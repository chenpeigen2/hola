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
import org.hyperledger.fabric.samples.querytransfer.config.ChaincodeConfig
import org.hyperledger.fabric.samples.querytransfer.directcall.DirectX
import org.hyperledger.fabric.samples.querytransfer.utils.LRUCache

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

    private val cache =
        LRUCache<String, String>(LRUCache.DEFAULT_CACHE_SZ)

    fun executeQuery(queryList: List<Query>, config: SgxConfig) = runBlocking {
        if (ChaincodeConfig.SUPPORT_CACHE) {
            cache.clear() // remove previous cache
        }
        queryList.forEach {
            it.resp = async(Dispatchers.Default) {
//                executeComplex(config, it.query).use { response ->
//                    response.body?.string()
//                }
                DirectX.executeComplex(it.query)
            }.await()
        }
    }

}