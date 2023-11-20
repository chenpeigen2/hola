package cn.ruc.edu.hola.client.dataservice

import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import java.io.File
import java.nio.file.Paths


object DataService {
    const val DEFAULT_LOCAL_MAP_NAME = "default_local_map"


    @JvmStatic
    @JvmOverloads
    fun writeFile(text: String, destFile: String = "./database/data.json") {
        val f = File(destFile)
        if (!f.exists()) {
            f.createNewFile()
        }
        f.writeText(text)
    }

    @JvmStatic
    @JvmOverloads
    fun readFile(destFile: String = "./database/data.json"): String {
        val f = File(destFile)
        if (!f.exists()) {
            f.createNewFile()
        }
        return f.readText()
    }

    @JvmStatic
    fun read(vertx: Vertx): ConfigRetriever {
        val pathToConfig = System.getProperty("vertx.config", "./database/data.json")
        println(Paths.get("./").toAbsolutePath().toString())
        val fileStore = ConfigStoreOptions()
            .setType("file")
            .setFormat("json")
            .setOptional(true)
            .setConfig(JsonObject().put("path", pathToConfig))
        val options = ConfigRetrieverOptions()
            .setScanPeriod(2000)
            .addStore(fileStore)
        return ConfigRetriever.create(vertx, options)
    }

}

