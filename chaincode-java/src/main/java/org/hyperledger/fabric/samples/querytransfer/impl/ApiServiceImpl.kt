package org.hyperledger.fabric.samples.querytransfer.impl

import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.hyperledger.fabric.samples.querytransfer.ApiService

@Deprecated(message = "using HttpClient instead")
internal class ApiServiceImpl : ApiService() {


    override fun complex(scheme: String, host: String, port: Int, text: String): Response {
        update(scheme, host, port)
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
        return client.newCall(request).execute()
    }

}
