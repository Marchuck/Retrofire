package com.jetbrains.jonnyzzz.myapplication.retrofire


interface Execution {
    fun execute(httpVerb: HttpVerb,
                url: String,
                requestBody: String,
                queries: Map<String, String>,
                onSuccess: (onResponse: String) -> Unit,
                onFailure: (throwable: Throwable) -> Unit): Cancellable
}