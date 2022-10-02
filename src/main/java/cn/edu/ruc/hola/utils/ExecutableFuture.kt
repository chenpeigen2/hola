package cn.edu.ruc.hola.utils

import cn.edu.ruc.hola.entity.Node
import okhttp3.Response
import java.util.concurrent.CompletableFuture

class ExecutableFuture(private val condition: String) : CompletableFuture<Response>() {
    fun getNode(): Node? {
        val response = get()
        return ResponseUtils.toNode(condition, response)
    }
}