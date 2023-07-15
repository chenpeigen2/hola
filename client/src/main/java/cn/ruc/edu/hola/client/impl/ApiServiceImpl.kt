package cn.ruc.edu.hola.client.impl

import cn.ruc.edu.hola.client.ApiService
import cn.ruc.edu.hola.client.bridge.ChainCodeBridge
import cn.ruc.edu.hola.client.encrypy.EncryptUtils
import cn.ruc.edu.hola.client.entity.RealResponse
import cn.ruc.edu.hola.client.entity.auth.AuthData
import cn.ruc.edu.hola.client.entity.auth.RSAAuthData
import cn.ruc.edu.hola.client.json.JsonUtils
import cn.ruc.edu.hola.client.utils.ChaincodeConfig
import cn.ruc.edu.hola.client.utils.RandomStringGenerator
import cn.ruc.edu.hola.client.utils.Timer
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.util.UUID

internal class ApiServiceImpl : ApiService() {

    private val bridge = ChainCodeBridge()

    /** client side treat as opacity execution **/
    override fun filterDocs(databaseName: String, filter: String): Response {
        val body = filter.toRequestBody("application/json".toMediaType())
        val url = baseUrl
            .addPathSegment("_find")
            .addQueryParameter("db_name", databaseName)
            .build()
        val request = Request.Builder()
            .url(url)
            .method("POST", body)
            .headers(headers.toHeaders())
//            .cacheControl()
            .tag("filterDocs${databaseName}")
            .build()
        return client.newCall(request).execute();
    }

    private val map = hashMapOf<String, String>()

    // TODO need acquire the return data
    private fun refresh() {
        val aesKey = RandomStringGenerator.generateAESKey()
        val data: AuthData = RSAAuthData(RandomStringGenerator.UID, aesKey);
        val text = JsonUtils.json.toJson(data)
        val body = text.toRequestBody("application/json".toMediaType())
        val url = baseUrl
            .addPathSegment("_auth")
            .build()
        val request = Request.Builder()
            .url(url)
            .method("POST", body)
            .headers(headers.toHeaders())
//            .cacheControl()
            .tag("_auth")
            .build()
        client.newCall(request).execute()
        map[baseUrl.toString()] = aesKey
    }

    override fun complex(requests: MutableList<cn.ruc.edu.hola.client.entity.Request>): RealResponse {
        if (Timer.shouldRefresh() || !map.containsKey(baseUrl.toString())) {
            refresh() //refresh the key
        }
        val aesKey = map[baseUrl.toString()]
        println(aesKey)
        val authedRequest = mutableListOf<cn.ruc.edu.hola.client.entity.Request>()
        requests.forEach {
            authedRequest.add(
                cn.ruc.edu.hola.client.entity.Request(
                    it.uid,
                    EncryptUtils.encryptAES(it.filter(), aesKey)
                )
            )
        }

        val text = JsonUtils.json.toJson(authedRequest)

        val response: RealResponse

        // expected config
        val sgxConfig = SgxConfig(scheme, host, port)
        val queryId = UUID.randomUUID().toString(); // use the uuid to delegate the queryId
        val owner = RandomStringGenerator.UID // use the query UID to delegate the owner
        val date = System.currentTimeMillis().toString() // use current date to delegate the query time
        val query = text // query encrypt text

        if (!ChaincodeConfig.supportNet()) {
            val body = text.toRequestBody("application/json".toMediaType())
            val url = baseUrl
                .addPathSegment("_complex")
                .build()
            val request = Request.Builder()
                .url(url)
                .method("POST", body)
                .headers(headers.toHeaders())
//            .cacheControl()
                .tag("complex")
                .build()
            response = RealResponse(client.newCall(request).execute(), aesKey)
        } else {
            // execute in the chaincode side
            response = RealResponse("chaincode response", aesKey)
        }

        return bridge.invokeQuery(sgxConfig, queryId, owner, date, query, response)
    }

    override fun getAESKey(): String? {
        return map[baseUrl.toString()]
    }

}
