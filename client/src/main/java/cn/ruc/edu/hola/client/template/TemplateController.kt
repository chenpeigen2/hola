package cn.ruc.edu.hola.client.template

import io.vertx.core.MultiMap
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import java.io.StringWriter

private const val TAG = "TemplateController"

object TemplateController {

    init {
        Velocity.init()
    }

    fun MultiMap.toVelocityContext(): VelocityContext {
        val context = VelocityContext()
        this.forEach { key, value ->
            context.put(key, value)
        }
        return context
    }

    fun String.evaluate(context: VelocityContext): String {
        val stringWriter = StringWriter()
        Velocity.evaluate(context, stringWriter, TAG, this)
        return stringWriter.toString()
    }

    fun String.isTemplated(): Boolean {
        return InternalTemplateController.isTemplated(this)
    }
}