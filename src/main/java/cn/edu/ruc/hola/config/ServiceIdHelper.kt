package cn.edu.ruc.hola.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.FileReader

object ServiceIdHelper {
    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    val hosts: List<Host> = readProps()

    private fun readProps(): List<Host> {
        return gson.fromJson(
            FileReader(
                ServiceIdHelper::class.java.classLoader.getResource("localsid.json")
                    ?.file ?: throw RuntimeException("error find sid file !")
            ), object : TypeToken<List<Host?>?>() {}.type
        )
    }

    fun sids(): List<String> {
        val ans: MutableList<String> = ArrayList()
        for (host in hosts) {
            ans.add(host.sid)
        }
        return ans
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