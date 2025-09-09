package ru.marinarodionova.recipesapp.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.marinarodionova.recipesapp.models.Category
import ru.marinarodionova.recipesapp.models.Recipe

interface RecipeApiService {
    @GET("recipe/{recipeId}")
    fun getRecipeById(
        @Path("recipeId") recipeId: Int
    ): Call<Recipe>

    @GET("recipes")
    fun getRecipesByIds(
        @Query("ids") ids: String
    ): Call<List<Recipe>>

    @GET("category/{categoryId}")
    fun getCategoryById(
        @Path("categoryId") categoryId: Int
    ): Call<Category>

    @GET("category/{categoryId}/recipes")
    fun getRecipesByCategoryId(
        @Path("categoryId") categoryId: Int
    ): Call<List<Recipe>>

    @GET("category")
    fun getCategories(): Call<List<Category>>
}