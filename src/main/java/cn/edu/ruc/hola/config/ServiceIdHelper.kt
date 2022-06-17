package cn.edu.ruc.hola.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.FileReader
import java.util.*

object ServiceIdHelper {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    @Volatile
    private var hosts: List<Host>? = null

    @Synchronized
    fun readProps(): List<Host> {
        if (hosts == null) {
            hosts = gson.fromJson<List<Host>>(
                FileReader(
                    Objects.requireNonNull(
                        ServiceIdHelper::class.java.classLoader.getResource("localsid.json")
                    ).file
                ), object : TypeToken<List<Host?>?>() {}.type
            )
        }
        return hosts!!
    }

    fun sids(): List<String> {
        val hosts = readProps()
        val ans: MutableList<String> = ArrayList()
        for (host in hosts) {
            ans.add(host.sid)
        }
        return ans
    }

    private const val DEFAULT_HOST = "127.0.0.1"
    private const val DEFAULT_PORT = 5984

    fun host(sid: String): Host? {
        val hosts = readProps()
        for (h in hosts) {
            if (sid == h.sid) {
                return h
            }
        }
        return null
    }
}