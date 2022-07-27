package cn.edu.ruc.hola.utils

import cn.edu.ruc.hola.config.Host
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * every couch db instance should have its own dbutils ,
 * just for design ,do not change .
 * if you are trying to change to static ,make sure Thread-safe, use ThreadLocal instead [not recommend !!!]
 */

data class CouchDBUtils(val host: String, private val port: Int) {

    private val logger = LoggingUtils.logger()

    companion object {
        private val client = OkHttpClient.Builder().build()

        private var dbs = ConcurrentHashMap<String, CouchDBUtils>()

        fun checkDbUtils(h: Host): CouchDBUtils {
            return dbs.computeIfAbsent(h.sid) {
                val u = CouchDBUtils(h.host, h.port)
                u
            }
        }
    }

    private var cookie: String? = null

    private val m =
        hashMapOf(Pair("Accept", "application/json"), Pair("Content-Type", "application/json"))

    private var authed = false

    private fun getBaseUrlBuilder(): HttpUrl.Builder {
        return HttpUrl.Builder().scheme("http")
            .host(host)
            .port(port)
    }

    suspend fun up(): Response = withContext(Dispatchers.IO) {
        val url = getBaseUrlBuilder()
            .addPathSegment("_up")
            .build()
        val r: Request = Request.Builder().url(url)
            .get()
            .build()
        return@withContext client.newCall(r).execute()
    }

    suspend fun uuids(cnt: Int): Response = withContext(Dispatchers.IO) {
        val url = getBaseUrlBuilder()
            .addPathSegment("_uuids")
            .addQueryParameter("count", cnt.toString()).build()
        val r: Request = Request.Builder().url(url)
            .get()
            .build()
        return@withContext client.newCall(r).execute()
    }

    suspend fun auth() = withContext(Dispatchers.IO) {
        val body: RequestBody = FormBody.Builder()
            .add("name", "admin")
            .add("password", "123456").build()
        val url = getBaseUrlBuilder()
            .addPathSegment("_session")
            .build()
        val r: Request = Request.Builder().url(url)
            .headers(m.toHeaders())
            .method("POST", body)
            .build()
        client.newCall(r).execute().use { res ->
            val cookieList = res.headers.values("Set-Cookie")
            cookie = cookieList[0]
            authed = true
            cookie?.let {
                m["Cookie"] = it
            }
        }
    }

    suspend fun createDb(db_name: String): Response = withContext(Dispatchers.IO) {
        if (!authed) auth()
        val headers: Headers = m.toHeaders()
        val body: RequestBody = FormBody.Builder().build()
        val url = getBaseUrlBuilder()
            .addPathSegment(db_name)
            .build()
        val r: Request = Request.Builder().url(url).headers(headers)
            .method("PUT", body)
            .build()
        return@withContext client.newCall(r).execute()
    }

    suspend fun allDbs(): Response = withContext(Dispatchers.IO) {
        if (!authed) auth()
        val headers: Headers = m.toHeaders()
        val url = getBaseUrlBuilder()
            .addPathSegment("_all_dbs")
            .build()
        val r: Request = Request.Builder().url(url).headers(headers)
            .get()
            .build()
        return@withContext client.newCall(r).execute()
    }

    suspend fun dbsInfo(data: String): Response = withContext(Dispatchers.IO) {
        if (!authed) auth()
        val headers: Headers = m.toHeaders()
        val url = getBaseUrlBuilder()
            .addPathSegment("_dbs_info")
            .build()
        val r: Request = Request.Builder().url(url).headers(headers)
            .get()
            .build()
        return@withContext client.newCall(r).execute()
    }

    suspend fun getDbInfo(db_name: String): Response = withContext(Dispatchers.IO) {
        if (!authed) auth()
        val headers: Headers = m.toHeaders()
        val url = getBaseUrlBuilder()
            .addPathSegment(db_name)
            .build()
        val r: Request = Request.Builder().url(url).headers(headers).build()
        return@withContext client.newCall(r).execute()
    }

    suspend fun deleteDb(db_name: String): Response = withContext(Dispatchers.IO) {
        if (!authed) auth()
        val headers: Headers = m.toHeaders()
        val url = getBaseUrlBuilder()
            .addPathSegment(db_name)
            .build()
        val r: Request = Request.Builder().url(url).headers(headers)
            .delete()
            .build()
        return@withContext client.newCall(r).execute()
    }

    suspend fun <T> addDoc(db_name: String, data: T): Response = withContext(Dispatchers.IO) {
        if (!authed) auth()
        val headers: Headers = m.toHeaders()
        var body: RequestBody? = null
        if (data is String) {
            body = data.toRequestBody("application/json".toMediaType())
        }
        if (data is File) {
            body = data.asRequestBody("application/json".toMediaType())
        }
        if (body == null) throw IllegalArgumentException("Type error")
        val url = getBaseUrlBuilder()
            .addPathSegment(db_name)
            .build()
        val r: Request = Request.Builder().url(url).headers(headers)
            .method("POST", body)
            .build()
        return@withContext client.newCall(r).execute()
    }

    suspend fun addBulkDoc(db_name: String, data: String): Response = withContext(Dispatchers.IO) {
        if (!authed) auth()
        val headers: Headers = m.toHeaders()
        val body: RequestBody = data.toRequestBody("application/json".toMediaType())
        val url = getBaseUrlBuilder()
            .addPathSegment(db_name)
            .addPathSegment("_bulk_docs")
            .build()
        val r: Request = Request.Builder().url(url).headers(headers)
            .method("POST", body)
            .build()
        return@withContext client.newCall(r).execute()
    }

    suspend fun deleteDoc(db_name: String, docId: String): Response = withContext(Dispatchers.IO) {
        if (!authed) auth()
        val headers: Headers = m.toHeaders()
        val url = getBaseUrlBuilder()
            .addPathSegment(db_name)
            .addPathSegment(docId)
            .build()
        val r: Request = Request.Builder().url(url).headers(headers)
            .delete()
            .build()
        return@withContext client.newCall(r).execute()
    }

    suspend fun allDocs(db_name: String): Response = withContext(Dispatchers.IO) {
        if (!authed) auth()
        val headers: Headers = m.toHeaders()
        val url = getBaseUrlBuilder()
            .addPathSegment(db_name)
            .addPathSegment("_all_docs")
            .build()
        val r: Request = Request.Builder().url(url).headers(headers)
            .build()
        return@withContext client.newCall(r).execute()
    }

    suspend fun findSpecDoc(db_name: String, docId: String): Response = withContext(Dispatchers.IO) {
        if (!authed) auth()
        val headers: Headers = m.toHeaders()
        val url = getBaseUrlBuilder()
            .addPathSegment(db_name)
            .addPathSegment(docId)
            .build()
        val r: Request = Request.Builder().url(url).headers(headers)
            .get()
            .build()
        return@withContext client.newCall(r).execute()
    }

    suspend fun findBulkDoc(db_name: String, data: String): Response = withContext(Dispatchers.IO) {
        if (!authed) auth()
        val headers: Headers = m.toHeaders()
        val body: RequestBody = data.toRequestBody("application/json".toMediaType())
        val url = getBaseUrlBuilder()
            .addPathSegment(db_name)
            .addPathSegment("_bulk_get")
            .build()
        val r: Request = Request.Builder().url(url).headers(headers)
            .method("POST", body)
            .build()
        return@withContext client.newCall(r).execute()
    }

    suspend fun filterDocs(db_name: String, filter: String): Response = withContext(Dispatchers.IO) {
        if (!authed) auth()
        val headers: Headers = m.toHeaders()
        val body: RequestBody = filter.toRequestBody("application/json".toMediaType())
        val url = getBaseUrlBuilder()
            .addPathSegment(db_name)
            .addPathSegment("_find")
            .build()
        val r: Request = Request.Builder().url(url).headers(headers)
            .method("POST", body)
            .build()
        return@withContext client.newCall(r).execute()
    }

    suspend fun createIndex(db_name: String, info: String): Response = withContext(Dispatchers.IO) {
        if (!authed) auth()
        val headers: Headers = m.toHeaders()
        val body: RequestBody = info.toRequestBody("application/json".toMediaType())
        val url = getBaseUrlBuilder()
            .addPathSegment(db_name)
            .addPathSegment("_index")
            .build()
        val r: Request = Request.Builder().url(url).headers(headers)
            .method("POST", body)
            .build()
        return@withContext client.newCall(r).execute()
    }

}