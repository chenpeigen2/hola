package cn.ruc.edu.hola.backbend.okhttp

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * every couch db instance should have its own dbutils ,
 * just for design ,do not change .
 * if you are trying to change to static ,make sure Thread-safe, use ThreadLocal instead [not recommend !!!]
 *
 *
 * change the kotlin run time to IO thread , to finish the Job
 */
data class CouchDBUtils(
    val host: String,
    private val port: Int,
    private val name: String,
    private val password: String
) {

    private val cookieJar = CookieJarImpl(MemoryCookieStore())

    private val basicParamsInterceptor = BasicParamsInterceptor.Builder().addHeaderParamsMap(m).build()

    /** split from companion object **/
    private val client = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .addInterceptor(basicParamsInterceptor)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .cache(Cache(File("./cachedir"), 10 * 1024 * 1024))
        .build()

    companion object {
        private val sdf = SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss 'GMT'", Locale.US)

        init {
            sdf.timeZone = TimeZone.getTimeZone("GMT") // 设置时区为GMT
        }

        private val m =
            hashMapOf(Pair("Accept", "application/json"), Pair("Content-Type", "application/json"))
    }

    private fun getBaseUrlBuilder(): HttpUrl.Builder {
        return HttpUrl.Builder().scheme("http")
            .host(host)
            .port(port)
    }

    fun up(): Response {
        val url = getBaseUrlBuilder()
            .addPathSegment("_up")
            .build()
        val r: Request = Request.Builder().url(url)
            .get()
            .build()
        return client.newCall(r).execute()
    }

    fun uuids(cnt: Int): Response {
        val url = getBaseUrlBuilder()
            .addPathSegment("_uuids")
            .addQueryParameter("count", cnt.toString()).build()
        val r: Request = Request.Builder().url(url)
            .get()
            .build()
        return client.newCall(r).execute()
    }

    private fun auth() {
        val body: RequestBody = FormBody.Builder()
            .add("name", name)
            .add("password", password).build()
        val url = getBaseUrlBuilder()
            .addPathSegment("_session")
            .build()
        val r: Request = Request.Builder().url(url)
            .method("POST", body)
            .build()
        client.newCall(r).execute()
    }

    private fun isExpired(url: HttpUrl): Boolean {
        return cookieJar.cookieStore.isExpired(url)
    }

    fun createDb(databaseName: String): Response {
        val body: RequestBody = FormBody.Builder().build()
        val url = getBaseUrlBuilder()
            .addPathSegment(databaseName)
            .build()
        if (isExpired(url)) auth()
        val r: Request = Request.Builder().url(url)
            .method("PUT", body)
            .build()
        client.newCall(r)
        return client.newCall(r).execute()
    }

    fun allDbs(): Response {
        val url = getBaseUrlBuilder()
            .addPathSegment("_all_dbs")
            .build()
        if (isExpired(url)) auth()
        val r: Request = Request.Builder().url(url)
            .get()
            .build()
        return client.newCall(r).execute()
    }

    fun dbsInfo(data: String): Response {
        val url = getBaseUrlBuilder()
            .addPathSegment("_dbs_info")
            .build()
        if (isExpired(url)) auth()
        val r: Request = Request.Builder().url(url)
            .get()
            .build()
        return client.newCall(r).execute()
    }

    fun getDbInfo(databaseName: String): Response {
        val url = getBaseUrlBuilder()
            .addPathSegment(databaseName)
            .build()
        if (isExpired(url)) auth()
        val r: Request = Request.Builder().url(url).build()
        return client.newCall(r).execute()
    }

    fun deleteDb(databaseName: String): Response {
        val url = getBaseUrlBuilder()
            .addPathSegment(databaseName)
            .build()
        if (isExpired(url)) auth()
        val r: Request = Request.Builder().url(url)
            .delete()
            .build()
        return client.newCall(r).execute()
    }

    fun <T> addDoc(databaseName: String, data: T): Response {
        var body: RequestBody? = null
        if (data is String) {
            body = data.toRequestBody("application/json".toMediaType())
        }
        if (data is File) {
            body = data.asRequestBody("application/json".toMediaType())
        }
        if (body == null) throw IllegalArgumentException("Type error")
        val url = getBaseUrlBuilder()
            .addPathSegment(databaseName)
            .build()
        if (isExpired(url)) auth()
        val r: Request = Request.Builder().url(url)
            .method("POST", body)
            .build()
        return client.newCall(r).execute()
    }

    fun addBulkDoc(databaseName: String, data: String): Response {
        val body: RequestBody = data.toRequestBody("application/json".toMediaType())
        val url = getBaseUrlBuilder()
            .addPathSegment(databaseName)
            .addPathSegment("_bulk_docs")
            .build()
        if (isExpired(url)) auth()
        val r: Request = Request.Builder().url(url)
            .method("POST", body)
            .build()
        return client.newCall(r).execute()
    }

    fun deleteDoc(databaseName: String, docId: String): Response {
        val url = getBaseUrlBuilder()
            .addPathSegment(databaseName)
            .addPathSegment(docId)
            .build()
        if (isExpired(url)) auth()
        val r: Request = Request.Builder().url(url)
            .delete()
            .build()
        return client.newCall(r).execute()
    }

    fun allDocs(databaseName: String): Response {
        val url = getBaseUrlBuilder()
            .addPathSegment(databaseName)
            .addPathSegment("_all_docs")
            .build()
        if (isExpired(url)) auth()
        val r: Request = Request.Builder().url(url)
            .build()
        return client.newCall(r).execute()
    }

    fun findSpecDoc(databaseName: String, docId: String): Response {
        val url = getBaseUrlBuilder()
            .addPathSegment(databaseName)
            .addPathSegment(docId)
            .build()
        if (isExpired(url)) auth()
        val r: Request = Request.Builder().url(url)
            .get()
            .build()
        return client.newCall(r).execute()
    }

    fun findBulkDoc(databaseName: String, data: String): Response {
        val body: RequestBody = data.toRequestBody("application/json".toMediaType())
        val url = getBaseUrlBuilder()
            .addPathSegment(databaseName)
            .addPathSegment("_bulk_get")
            .build()
        if (isExpired(url)) auth()
        val r: Request = Request.Builder().url(url)
            .method("POST", body)
            .build()
        return client.newCall(r).execute()
    }

    fun filterDocs(databaseName: String, filter: String): Response {
        val body: RequestBody = filter.toRequestBody("application/json".toMediaType())
        val url = getBaseUrlBuilder()
            .addPathSegment(databaseName)
            .addPathSegment("_find")
            .build()
        if (isExpired(url)) auth()
        val r: Request = Request.Builder().url(url)
            .method("POST", body)
            .build()
        return client.newCall(r).execute()
    }

    fun createIndex(databaseName: String, info: String): Response {
        val body: RequestBody = info.toRequestBody("application/json".toMediaType())
        val url = getBaseUrlBuilder()
            .addPathSegment(databaseName)
            .addPathSegment("_index")
            .build()
        if (isExpired(url)) auth()
        val r: Request = Request.Builder().url(url)
            .method("POST", body)
            .build()
        return client.newCall(r).execute()
    }

}