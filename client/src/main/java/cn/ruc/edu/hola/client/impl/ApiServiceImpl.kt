package cn.ruc.edu.hola.client.impl

import cn.ruc.edu.hola.client.ApiService
import cn.ruc.edu.hola.client.Query
import cn.ruc.edu.hola.client.bridge.ChainCodeBridge
import cn.ruc.edu.hola.client.encrypy.EncryptUtils
import cn.ruc.edu.hola.client.entity.ChaincodeRequest
import cn.ruc.edu.hola.client.entity.RealResponse
import cn.ruc.edu.hola.client.entity.auth.AuthData
import cn.ruc.edu.hola.client.entity.auth.RSAAuthData
import cn.ruc.edu.hola.client.json.JsonUtils
import cn.ruc.edu.hola.client.config.ChaincodeConfig
import cn.ruc.edu.hola.client.utils.RandomStringGenerator
import cn.ruc.edu.hola.client.utils.Timer
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.util.*

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

    // test single request
    override fun complex(chaincodeRequests: MutableList<ChaincodeRequest>): RealResponse {
        if (Timer.shouldRefresh() || !map.containsKey(baseUrl.toString())) {
            refresh() //refresh the key
        }
        val aesKey = map[baseUrl.toString()]
        println(aesKey)
        val authedChaincodeRequest = mutableListOf<ChaincodeRequest>()
        chaincodeRequests.forEach {
            authedChaincodeRequest.add(
                ChaincodeRequest(
                    it.uid,
                    EncryptUtils.encryptAES(it.filter(), aesKey)
                )
            )
        }

        val text = JsonUtils.json.toJson(authedChaincodeRequest)

        val response: RealResponse

        // expected config
        var sgxConfig = SgxConfig(scheme, host, port)
        val queryId = UUID.randomUUID().toString(); // use the uuid to delegate the queryId
        val owner = RandomStringGenerator.UID // use the query UID to delegate the owner
        val date = System.currentTimeMillis().toString() // use current date to delegate the query time
        val query = text // query encrypt text

        if (!ChaincodeConfig.SUPPORT_NET) {
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
            // replace with our own config
            sgxConfig = SgxConfig(sgxConfig.scheme, "172.17.0.1", sgxConfig.port)
            // execute in the chaincode side
            response = RealResponse("chaincode response", aesKey)
        }

        return bridge.invokeQuery(sgxConfig, queryId, owner, date, query, response)
    }

    override fun direct_sgx(chaincodeRequests: MutableList<ChaincodeRequest>): RealResponse {
        val aesKey = "OQhDAflfrteVkmUvj3ZK7SGD"
        val authedChaincodeRequest = mutableListOf<ChaincodeRequest>()
        chaincodeRequests.forEach {
            authedChaincodeRequest.add(
                ChaincodeRequest(
                    it.uid,
                    EncryptUtils.encryptAES(it.filter(), aesKey)
                )
            )
        }

        val text = JsonUtils.json.toJson(authedChaincodeRequest)

        val response: RealResponse

        // expected config
        var sgxConfig = SgxConfig(scheme, host, port)
        val queryId = UUID.randomUUID().toString(); // use the uuid to delegate the queryId
        val owner = RandomStringGenerator.UID // use the query UID to delegate the owner
        val date = System.currentTimeMillis().toString() // use current date to delegate the query time
        val query = text // query encrypt text

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

        return bridge.invokeQuery(sgxConfig, queryId, owner, date, query, response)
    }

    override fun direct_complex(text: String): RealResponse {
        val body = text.toRequestBody("application/json".toMediaType())
        val url = baseUrl
            .addPathSegment("_find")
            .build()
        val request = Request.Builder()
            .url(url)
            .method("POST", body)
            .headers(headers.toHeaders())
//            .cacheControl()
            .tag("complex")
            .build()
        return RealResponse(client.newCall(request).execute().body!!.string(), "OQhDAflfrteVkmUvj3ZK7SGD")
    }

    // test multi requests
    override fun multiComplex(chaincodeRequests: MutableList<MutableList<ChaincodeRequest>>): MutableList<RealResponse> {
        val aesKey = "OQhDAflfrteVkmUvj3ZK7SGD"
        /*println(aesKey)*/
        val authChaincodeRequests = mutableListOf<MutableList<ChaincodeRequest>>()

        chaincodeRequests.forEach {
            val list = mutableListOf<ChaincodeRequest>()
            it.forEach {
                list.add(
                    ChaincodeRequest(
                        it.uid,
                        EncryptUtils.encryptAES(it.filter(), aesKey)
                    )
                )
            }
            authChaincodeRequests.add(list)
        }

        // expected config
        var sgxConfig = SgxConfig(scheme, host, port)
        val queryId = UUID.randomUUID().toString(); // use the uuid to delegate the queryId
        val owner = RandomStringGenerator.UID // use the query UID to delegate the owner
        val date = System.currentTimeMillis().toString() // use current date to delegate the query time

        // replace with our own config
        sgxConfig = SgxConfig(sgxConfig.scheme, "172.17.0.1", sgxConfig.port)

        val sz = chaincodeRequests.size
        val queries = mutableListOf<Query>()
        val resp = mutableListOf<RealResponse>()

        for (i in 0 until sz) {
            val response = RealResponse("chaincode response", aesKey)
            queries.add(Query(queryId, owner, date, JsonUtils.json.toJson(authChaincodeRequests[i]), response.response))
            resp.add(response)
        }

        return bridge.invokeMultiQueries(sgxConfig, queries, resp)
    }

    override fun getAESKey(): String? {
        return map[baseUrl.toString()]
    }

}
