package ru.marinarodionova.recipesapp.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.marinarodionova.recipesapp.models.Category
import ru.marinarodionova.recipesapp.models.Recipe

interface RecipeApiService {
    @GET("recipe/{recipeId}")
    suspend fun getRecipeById(
        @Path("recipeId") recipeId: Int
    ): Recipe

    @GET("recipes")
    suspend fun getRecipesByIds(
        @Query("ids") ids: String
    ): List<Recipe>

    @GET("category/{categoryId}")
    suspend fun getCategoryById(
        @Path("categoryId") categoryId: Int
    ): Category

    @GET("category/{categoryId}/recipes")
    suspend fun getRecipesByCategoryId(
        @Path("categoryId") categoryId: Int
    ): List<Recipe>

    @GET("category")
    suspend fun getCategories(): List<Category>
}