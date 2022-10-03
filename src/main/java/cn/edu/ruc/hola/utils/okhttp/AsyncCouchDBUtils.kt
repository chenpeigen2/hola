package cn.edu.ruc.hola.utils.okhttp

import cn.edu.ruc.hola.config.Host
import cn.edu.ruc.hola.fake.FakeConfig
import cn.edu.ruc.hola.utils.LoggingUtils
import okhttp3.*
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * every couch db instance should have its own dbutils ,
 * just for design ,do not change .
 * if you are trying to change to static ,make sure Thread-safe, use ThreadLocal instead [not recommend !!!]
 *
 *
 * change the kotlin run time to IO thread , to finish the Job
 */
data class AsyncCouchDBUtils(val host: String, private val port: Int) {

    private val logger = LoggingUtils.logger()

    companion object {

        private val client = OkHttpClient.Builder()
            .cache(Cache(File("./cachedir"), 10 * 1024 * 1024))
            .build()

        private var dbs = ConcurrentHashMap<String, AsyncCouchDBUtils>()

        fun checkDbUtils(h: Host): AsyncCouchDBUtils {
            return dbs.computeIfAbsent(h.sid) {
                val u = AsyncCouchDBUtils(h.host, h.port)
                u
            }
        }

        private val sdf = SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss 'GMT'", Locale.US)

        init {
            sdf.timeZone = TimeZone.getTimeZone("GMT") // 设置时区为GMT
        }
    }


    private var cookie: String? = null

    private val m =
        hashMapOf(Pair("Accept", "application/json"), Pair("Content-Type", "application/json"))

    private fun getBaseUrlBuilder(): HttpUrl.Builder {
        return HttpUrl.Builder().scheme("http")
            .host(host)
            .port(port)
    }

    private fun auth() {
        val body: RequestBody = FormBody.Builder()
            .add("name", FakeConfig.name)
            .add("password", FakeConfig.password).build()
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
            cookie?.let {
                if (it == "") return@let
                val timeString = it.substringAfter("Expires=").substringBefore(";")
                expiresTime = sdf.parse(timeString).time
                m["Cookie"] = it
            }
        }
    }

    private var expiresTime: Long = -1L

    private fun isExpired(): Boolean {
        if (expiresTime == -1L) return true
        if (expiresTime > Date().time) return false
        return true
    }

    fun filterDocs(db_name: String, filter: String, callback: OkHttpResponseFuture) {
        if (isExpired()) auth()
        val headers: Headers = m.toHeaders()
        val body: RequestBody = filter.toRequestBody("application/json".toMediaType())
        val url = getBaseUrlBuilder()
            .addPathSegment(db_name)
            .addPathSegment("_find")
            .build()
        val r: Request = Request.Builder().url(url).headers(headers)
            .method("POST", body)
            .build()
        client.newCall(r).enqueue(callback)
    }

}