package cn.edu.ruc.hola.utils

import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.models.couchdb.FilterDocs
import okhttp3.Call
import okhttp3.Response
import java.io.IOException


class OkHttpCallBackImpl : OkHttpCallBack {

    private val gson = ServerHelper.gson

    lateinit var node: Node

    lateinit var condition: String

    override fun onFailure(call: Call, e: IOException) {

    }

    @Throws(IOException::class)
    override fun onResponse(call: Call, res: Response) {
        res.body?.let {
            val filterDocs: FilterDocs =
                gson.fromJson(
                    it.string(),
                    FilterDocs::class.java
                )

            val data = buildString {
                filterDocs.docs?.forEach { doc ->
                    append(doc.data.toString())
                }
            }

            node = Node(condition, data)
        }

    }
}