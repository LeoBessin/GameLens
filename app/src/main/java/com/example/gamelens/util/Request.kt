package com.example.gamelens.util
import okhttp3.OkHttpClient
import okhttp3.Request

object Request {
    private val client = OkHttpClient()

    fun sendGet(url: String): String {
        println("url : $url")
        val request = Request.Builder().url(url).build()
        return client.newCall(request).execute().use {
            if (!it.isSuccessful) {
                throw Exception("Erreur serveur :${it.code}\n${it.body.string()}")
            }
            it.body.string()
        }
    }

}