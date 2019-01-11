package com.jetbrains.jonnyzzz.myapplication.retrofire

class Retrofire(val execution: Execution) {

    private var url: String = ""
    private var requestBody = "{}"
    private var queries = emptyMap<String, String>()
    private var httpVerb: HttpVerb = HttpVerb.GET

    fun withHttpVerb(httpVerb: HttpVerb): Retrofire {
        this.httpVerb = httpVerb
        return this
    }

    fun withEndpoint(url: String): Retrofire {
        this.url = url
        return this
    }

    fun withQuery(key: String, value: String): Retrofire {
        queries += Pair(key, value)
        return this
    }

    fun withRequestBody(body: String): Retrofire {
        requestBody = body
        return this
    }

    fun execute(onSuccess: ((onResponse: String) -> Unit),
                onFailure: ((throwable: Throwable) -> Unit)): Cancellable {
        return execution.execute(httpVerb,
                url,
                requestBody,
                queries,
                onSuccess,
                onFailure)
    }
}