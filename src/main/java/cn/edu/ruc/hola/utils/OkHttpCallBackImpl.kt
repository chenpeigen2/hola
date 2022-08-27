package cn.edu.ruc.hola.utils

import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.models.couchdb.FilterDocs
import kotlinx.coroutines.channels.Channel
import okhttp3.Call
import okhttp3.Response
import java.io.IOException

val channel = Channel<Node>()


class OkHttpCallBackImpl : OkHttpCallBack() {

    private val gson = ServerHelper.gson


    lateinit var condition: String

    override fun doResponse(call: Call, res: Response) {
//        res.body?.let {
//            val filterDocs: FilterDocs =
//                gson.fromJson(
//                    it.string(),
//                    FilterDocs::class.java
//                )
//
//            val data = buildString {
//                filterDocs.docs?.forEach { doc ->
//                    append(doc.data.toString())
//                }
//            }
//
//            val node = Node(condition, data)
//        }
    }


    override fun doFail(call: Call, e: IOException) {
        TODO("Not yet implemented")
    }
}