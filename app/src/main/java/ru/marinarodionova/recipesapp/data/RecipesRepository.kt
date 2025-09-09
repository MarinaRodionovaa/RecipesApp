package ru.marinarodionova.recipesapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import ru.marinarodionova.recipesapp.models.Category
import ru.marinarodionova.recipesapp.models.Recipe
import java.util.concurrent.Executors
import java.util.concurrent.Callable

class RecipesRepository {
    private val executor = Executors.newFixedThreadPool(10)
    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    fun getCategories(): List<Category>? {
        var categories: List<Category>? = null
        val categoriesApi = executor.submit(Callable {
            try {
                val categoriesCall: Call<List<Category>> = service.getCategories()
                val categoriesResponse: Response<List<Category>> = categoriesCall.execute()
                categories = categoriesResponse.body()
                categories
            } catch (e: Exception) {
                null
            }
        })
        categoriesApi.get()
        return categories
    }

    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        var recipeList: List<Recipe>? = null
        val getRecipeList = executor.submit(Callable {
            try {
                val recipeListCall: Call<List<Recipe>> = service.getRecipesByCategoryId(categoryId)
                val recipeListResponse: Response<List<Recipe>> = recipeListCall.execute()
                recipeList = recipeListResponse.body()
                recipeList
            } catch (e: Exception) {
                null
            }
        })
        getRecipeList.get()
        return recipeList
    }

    fun getCategoryByCategoryId(categoryId: Int): Category? {
        var category: Category? = null
        val getCategory = executor.submit(Callable {
            try {
                val categoryCall: Call<Category> = service.getCategoryById(categoryId)
                val categoryResponse: Response<Category> = categoryCall.execute()
                category = categoryResponse.body()
                category
            } catch (e: Exception) {
                null
            }
        })
        getCategory.get()
        return category
    }

    fun getRecipeById(recipeId: Int): Recipe? {
        var recipe: Recipe? = null
        val getRecipe = executor.submit(Callable {
            try {
                val recipeCall: Call<Recipe> = service.getRecipeById(recipeId)
                val recipeResponse: Response<Recipe> = recipeCall.execute()
                recipe = recipeResponse.body()
                recipe
            } catch (e: Exception) {
                null
            }
        })
        getRecipe.get()
        return recipe
    }

    fun getRecipesById(idSet: Set<Int>): List<Recipe>? {
        var recipes: List<Recipe>? = null
        val getRecipes = executor.submit(Callable {
            try {
                val recipeListCall: Call<List<Recipe>> =
                    service.getRecipesByIds(idSet.joinToString(","))
                val recipeListResponse: Response<List<Recipe>> = recipeListCall.execute()
                recipes = recipeListResponse.body()
                recipes
            } catch (e: Exception) {
                null
            }
        })
        getRecipes.get()
        return recipes

    }
}