package cn.ruc.edu

import okhttp3.*
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.*

data class Dao(val host: String, private val port: Int) {

    private val client = OkHttpClient.Builder().build()

    private val sdf = SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss 'GMT'", Locale.US)

    init {
        sdf.timeZone = TimeZone.getTimeZone("GMT") // 设置时区为GMT
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


    fun filterDocs(db_name: String, filter: String): Response {
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
        return client.newCall(r).execute()
    }

}