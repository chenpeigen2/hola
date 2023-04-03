package cn.ruc.edu.hola.client.impl

import cn.ruc.edu.hola.client.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class ApiServiceImpl : ApiService() {

    /** client side treat as opacity execution **/
    override fun filterDocs(databaseName: String, filter: String): Response {
        TODO("Not yet implemented")
    }

}
