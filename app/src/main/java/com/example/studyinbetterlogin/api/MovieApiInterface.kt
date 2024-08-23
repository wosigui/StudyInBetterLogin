package com.example.studyinbetterlogin.api

import com.example.studyinbetterlogin.model.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 *
 * 暴露给外部的访问方法，类似于room里面的Dao
 * Get:从网络中获取对应的数据 请求参数在URL上
 *  只需要传递请求参数 而不需要上传文件
 * Post：从网络中获取对应的数据 请求参数在请求体中
 * Delete：删除对应服务器对应的数据库里面的数据
 * Put：更新服务器的全部数据
 * Patch：更新服务器对应的数据（部分更新）
 * Head：只是响应的头部信息，不会下载数据
 */
interface MovieApiInterface {
    @GET("movies?")
    suspend fun getMovies(@Query("page") index:Int): Response<Movies>
//
//    @GET("login?name=jack&password=123")
//    suspend fun login(@Query("name") name:String , @Query("password") pwd:String)
//
//    @GET("login?")
//    suspend fun login(@QueryMap maps:Map<String,String>)
}