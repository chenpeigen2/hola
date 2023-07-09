package org.hyperledger.fabric.samples.querytransfer.json

import com.google.gson.GsonBuilder

object JsonUtils {

    val json = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
}