package com.example.kp_2024_bd

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import models.Auth
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit.AuthRetrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var txtUser: EditText
    private lateinit var txtPass: EditText
    private lateinit var buAuthorization: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        txtUser = findViewById(R.id.txtUser)
        txtPass = findViewById(R.id.txtPass)
        buAuthorization = findViewById(R.id.buAuthorization)

        buAuthorization.setOnClickListener {
            val username = txtUser.text.toString()
            val password = txtPass.text.toString()


            Auth.user = username
            Auth.pass = password

            val basicAuth = Credentials.basic(username, password)

            val httpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .header("Authorization", basicAuth)
                        .build()
                    chain.proceed(request)
                }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = retrofit.create(AuthRetrofit::class.java).authCon()?.execute()
                    if (response != null) {
                        if (response.isSuccessful) {
                            val message = response.body()
                            showToast((message ?: "Успешная авторизация") as String)

                            val intent = Intent(this@MainActivity, ListActivity::class.java)
                            startActivity(intent)
                        } else {
                            showToast("Проверьте данные входа")
                        }
                    }
                } catch (e: IOException) {
                    showToast("Ошибка аутентификации")
                }
            }
        }

    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}