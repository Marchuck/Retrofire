package org.kotlin.mpp.mobile.com.jetbrains.jonnyzzz.myapplication.tasks

import com.jetbrains.jonnyzzz.myapplication.retrofire.Cancellable
import com.jetbrains.jonnyzzz.myapplication.retrofire.Execution
import com.jetbrains.jonnyzzz.myapplication.retrofire.HttpVerb
import com.jetbrains.jonnyzzz.myapplication.retrofire.Retrofire
import org.kotlin.mpp.mobile.com.jetbrains.jonnyzzz.myapplication.main.Main

class LoginApi(private val endpoint: String,
               private val execution: Execution,
               private val main: Main) {

    fun login(email: String,
              password: String,
              user: ((user: User?) -> Unit)): Cancellable {

        return Retrofire(execution)
                .withHttpVerb(HttpVerb.POST)
                .withEndpoint("${endpoint}/login")
                .withRequestBody("{ \"email\" : \"$email\", \"password\" : \"$password\" }")
                .execute({
                    main.post {
                        user(User(it))
                    }
                }, {
                    main.post {
                        user(null)
                    }
                })
    }

    fun logout() {

    }

}