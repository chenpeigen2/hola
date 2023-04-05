package cn.ruc.edu.hola.client.impl

import cn.ruc.edu.hola.client.ApiService
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

internal class ApiServiceImpl : ApiService() {

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

}
