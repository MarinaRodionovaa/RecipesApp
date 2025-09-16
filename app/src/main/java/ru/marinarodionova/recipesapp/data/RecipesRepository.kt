package ru.marinarodionova.recipesapp.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import ru.marinarodionova.recipesapp.models.Category
import ru.marinarodionova.recipesapp.models.Recipe

class RecipesRepository {
    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    suspend fun getCategories(): List<Category>? {
        var categories: List<Category>? = null
        try {
            withContext(Dispatchers.IO) {
                val categoriesCall: Call<List<Category>> = service.getCategories()
                val categoriesResponse: Response<List<Category>> = categoriesCall.execute()
                categories = categoriesResponse.body()
            }
        } catch (e: Exception) {
            Log.i("!!!!", "getCategories failed")
        }
        return categories
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        var recipeList: List<Recipe>? = null
        try {
            withContext(Dispatchers.IO) {
                val recipeListCall: Call<List<Recipe>> = service.getRecipesByCategoryId(categoryId)
                val recipeListResponse: Response<List<Recipe>> = recipeListCall.execute()
                recipeList = recipeListResponse.body()
            }
        } catch (e: Exception) {
            Log.i("!!!!", "getRecipesByCategoryId failed")
        }

        return recipeList
    }

    suspend fun getCategoryByCategoryId(categoryId: Int): Category? {
        var category: Category? = null
        try {
            withContext(Dispatchers.IO) {

                val categoryCall: Call<Category> = service.getCategoryById(categoryId)
                val categoryResponse: Response<Category> = categoryCall.execute()
                category = categoryResponse.body()
            }
        } catch (e: Exception) {
            Log.i("!!!!", "getCategoryByCategoryId failed")
        }
        return category
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        var recipe: Recipe? = null
        try {
            withContext(Dispatchers.IO) {
                val recipeCall: Call<Recipe> = service.getRecipeById(recipeId)
                val recipeResponse: Response<Recipe> = recipeCall.execute()
                recipe = recipeResponse.body()
            }
        } catch (e: Exception) {
            Log.i("!!!!", "getRecipeById failed")
        }
        return recipe
    }

    suspend fun getRecipesById(idSet: Set<Int>): List<Recipe>? {
        var recipes: List<Recipe>? = null
        try {
            withContext(Dispatchers.IO) {
                val recipeListCall: Call<List<Recipe>> =
                    service.getRecipesByIds(idSet.joinToString(","))
                val recipeListResponse: Response<List<Recipe>> = recipeListCall.execute()
                recipes = recipeListResponse.body()
            }
        } catch (e: Exception) {
            Log.i("!!!!", "getRecipesById failed")
        }
        return recipes
    }
}