package com.example.studyinbetterlogin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

//  myapp://callback
class MainActivity : AppCompatActivity() {
    private val CLIENT_ID = "your_spotify_client_id"
    private val REDIRECT_URI = "myapp://callback"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val uri: Uri? = intent.data
        if (uri != null && uri.toString().startsWith("myapp://callback")) {
            val accessToken = uri.getQueryParameter("access_token")
            if (accessToken != null) {
                // 在这里使用accessToken调用Spotify API
            } else {
                // 处理没有获取到accessToken的情况
            }
        }
    }

}