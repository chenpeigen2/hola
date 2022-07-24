package cn.edu.ruc.hola.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.FileReader

/**
 * it is thread-safe
 */
object ServerHelper {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    val hosts: List<Host> = readProps()

    private fun readProps(): List<Host> {
        return gson.fromJson(
            FileReader(
                ServerHelper::class.java.classLoader.getResource("localsid.json")
                    ?.file ?: throw RuntimeException("error find sid file !")
            ), object : TypeToken<List<Host?>?>() {}.type
        )
    }

    val sids: List<String>
        get() {
            val sids: MutableList<String> = ArrayList()
            for (host in hosts) {
                sids.add(host.sid)
            }
            return sids
        }

    fun host(sid: String): Host? {
        for (h in hosts) {
            if (sid == h.sid) {
                return h
            }
        }
        return null
    }
}