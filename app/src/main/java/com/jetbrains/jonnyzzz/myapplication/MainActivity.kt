package com.jetbrains.jonnyzzz.myapplication

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.jetbrains.jonnyzzz.myapplication.retrofire.Cancellable
import com.jetbrains.jonnyzzz.myapplication.retrofire.OkHttpExecution
import org.json.JSONObject
import org.kotlin.mpp.mobile.com.jetbrains.jonnyzzz.myapplication.main.Main
import org.kotlin.mpp.mobile.com.jetbrains.jonnyzzz.myapplication.tasks.LoginApi
import org.kotlin.mpp.mobile.createApplicationScreenMessage

class MainActivity : AppCompatActivity() {

    class AndroidMain : Main {

        private val handler by lazy { Handler(Looper.getMainLooper()) }

        override fun post(task: () -> Unit) {
            handler.post {
                task()
            }
        }
    }

    val main_text by lazy { findViewById<TextView>(R.id.main_text) }
    val loginButton by lazy { findViewById<Button>(R.id.loginButton) }
    val progressBar by lazy { findViewById<View>(R.id.progressBar) }
    val errorLabel by lazy { findViewById<TextView>(R.id.errorLabel) }
    val emailInput by lazy { findViewById<EditText>(R.id.emailInput) }
    val passwordInput by lazy { findViewById<EditText>(R.id.passwordInput) }

    val main: Main = AndroidMain()

    var cancellable: Cancellable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_text.text = createApplicationScreenMessage()

        loginButton.setOnClickListener {

            progressBar.visibility = VISIBLE
            errorLabel.text = ""

            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            cancellable = LoginApi(BuildConfig.ENDPOINT, OkHttpExecution(), main)
                    .login(email, password) { user ->

                        progressBar.visibility = GONE

                        if (user == null) {
                            errorLabel.setTextColor(Color.RED)
                            errorLabel.text = "User not found"
                        } else {
                            errorLabel.setTextColor(ContextCompat.getColor(this, R.color.green))

                            val userData = user.json.toJson().getJSONObject("data");
                            errorLabel.text = "(${userData.getDouble("latitude")}, ${userData.getDouble("longitude")})"
                        }
                    }
        }
    }

    fun String.toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        cancellable?.cancel()
    }
}
