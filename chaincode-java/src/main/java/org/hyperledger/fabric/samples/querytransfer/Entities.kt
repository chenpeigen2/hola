package org.hyperledger.fabric.samples.querytransfer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose

val json: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

data class SGXEntity(
    @Expose() val scheme: String,
    @field:Expose val host: String,
    @field:Expose val port: Int
)