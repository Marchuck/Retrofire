package com.jetbrains.jonnyzzz.myapplication.retrofire

import okhttp3.*
import java.io.IOException

class OkHttpExecution : Execution {

    val okHttpClient = OkHttpClient()

    val JSON = MediaType.parse("application/json; charset=utf-8")

    override fun execute(httpVerb: HttpVerb,
                         url: String,
                         requestBody: String,
                         queries: Map<String, String>,
                         onSuccess: (onResponse: String) -> Unit,
                         onFailure: (throwable: Throwable) -> Unit): Cancellable {

        var call: Call? = null

        if (httpVerb == HttpVerb.GET) {
            val urlBuilder = HttpUrl.parse(url)!!.newBuilder()
            for (query in queries) {
                urlBuilder.addQueryParameter(query.key, query.value)
            }

            val fullUrl = urlBuilder.build().toString()

            val request = Request.Builder()
                    .url(fullUrl)
                    .build()
            call = execute(request, onSuccess, onFailure)

        } else if (httpVerb == HttpVerb.POST) {
            val body = RequestBody.create(JSON, requestBody)
            val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()
            call = execute(request, onSuccess, onFailure)
        }

        return CancellableCall(call)
    }

    fun execute(request: Request, onSuccess: (onResponse: String) -> Unit,
                onFailure: (throwable: Throwable) -> Unit): Call {
        val call = okHttpClient.newCall(request)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                onSuccess(response.body()?.string() ?: response.body().toString())
            }
        })
        return call
    }


    class CancellableCall(private val call: Call?) : Cancellable {
        override fun cancel() {
            call?.cancel()
        }
    }
}