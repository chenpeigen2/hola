package cn.edu.ruc.hola.utils

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


abstract class OkHttpCallBack : Callback {

    abstract fun doResponse(call: Call, response: Response)

    abstract fun doFail(call: Call, e: IOException)


    override fun onResponse(call: Call, response: Response) {
        doResponse(call, response)
    }

    override fun onFailure(call: Call, e: IOException) {
        doFail(call, e)
    }
}