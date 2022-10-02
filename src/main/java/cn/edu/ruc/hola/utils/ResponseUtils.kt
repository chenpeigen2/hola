package cn.edu.ruc.hola.utils

import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.models.couchdb.FilterDocs
import okhttp3.Response

object ResponseUtils {
    private val gson = ServerHelper.gson

    fun toNode(condition: String, res: Response): Node {
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

            return Node(condition, data)
        }
    }
}