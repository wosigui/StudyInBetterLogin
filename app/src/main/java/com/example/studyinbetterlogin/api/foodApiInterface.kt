package com.example.studyinbetterlogin.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface foodApiInterface {
    @GET("recipes/complexSearch")
    fun searchRecipes(
        @Query("query") query: String,
        @Query("diet") diet: String,
        @Query("type") type: String,
        @Query("instructionsRequired") instructionsRequired: Boolean,
        @Query("fillIngredients") fillIngredients: Boolean,
        @Query("number") number: Int,
        @Query("addRecipeInformation") addRecipeInformation: Boolean,
        @Query("apiKey") apiKey: String
    ): Call<RecipeResponse>
}


object RetrofitClient {

    private const val BASE_URL = "https://api.spoonacular.com/"

    val instance: foodApiInterface by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(foodApiInterface::class.java)
    }
}

data class RecipeResponse(
    val results: List<Recipe>
)

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val instructions: String,
    val diet: String
)