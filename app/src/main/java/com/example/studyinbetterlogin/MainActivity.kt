package com.example.studyinbetterlogin


import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.example.studyinbetterlogin.api.Constants
import com.example.studyinbetterlogin.api.MovieApiInterface
import com.example.studyinbetterlogin.model.Movies
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        lifecycleScope.launch (Dispatchers.IO){
//            val url = URL("https://moviesapi.ir/api/v1/movies?page1")
//            val connection = url.openConnection() as HttpURLConnection
//            if(connection.responseCode == HttpURLConnection.HTTP_OK){
//                //有内容才开始下载
//                //获取详细的请求头部信息
//                connection.headerFields.entries.forEach{entry ->
//                    Log.v("MainActivity","Key:${entry.key}\n")
//                    Log.v("MainActivity","Value:${entry.value}\n")
//                }
//                BufferedReader(InputStreamReader(connection.inputStream)).use {br ->
//                    val content=br.readText()
//                    val movies = Gson().fromJson(content, Movies::class.java)
//                    Log.d("MainActivity","$movies")
//                }
//            }
//        }

    }
}