package cn.edu.ruc.hola.utils.okhttp

import cn.edu.ruc.hola.config.ServerHelper
import cn.edu.ruc.hola.entity.Node
import cn.edu.ruc.hola.models.couchdb.FilterDocs
import okhttp3.Response
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse
import java.util.concurrent.Future

/**
 * http response utils for handle the response for http-client or okhttp
 */
object ResponseUtils {

    private val gson = ServerHelper.gson

    fun toNode(condition: String, res: Response): Node? {
        res.body?.let {
            return toNode(condition, it.string())
        }
        return null
    }

    fun toNode(condition: String, content: String): Node {
        content.let {
            val filterDocs: FilterDocs =
                gson.fromJson(
                    it,
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

    fun Future<SimpleHttpResponse>.getNode(condition: String): Node {
        return toNode(condition, get().bodyText)
    }
}