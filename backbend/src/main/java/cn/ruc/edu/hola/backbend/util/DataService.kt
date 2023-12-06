package cn.ruc.edu.hola.backbend.util

import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import java.io.File
import java.nio.file.Paths

object DataService {

    @JvmStatic
    fun writeFile(text: String, destFile: String) {
        val f = File(destFile)
        if (!f.exists()) {
            f.createNewFile()
        }
        f.writeText(text)
    }

    @JvmStatic
    fun readFile(destFile: String): String {
        val f = File(destFile)
        if (!f.exists()) {
            f.createNewFile()
        }
        return f.readText()
    }

    @JvmStatic
    fun read(vertx: Vertx, path: String): ConfigRetriever {
        println(Paths.get("./").toAbsolutePath().toString())
        val fileStore = ConfigStoreOptions()
            .setType("file")
            .setFormat("json")
            .setOptional(true)
            .setConfig(JsonObject().put("path", path))
        val options = ConfigRetrieverOptions()
            .setScanPeriod(2000)
            .addStore(fileStore)
        return ConfigRetriever.create(vertx, options)
    }

}

