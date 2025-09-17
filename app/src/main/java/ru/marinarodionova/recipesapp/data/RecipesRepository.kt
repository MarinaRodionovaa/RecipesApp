package ru.marinarodionova.recipesapp.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.marinarodionova.recipesapp.models.Category
import ru.marinarodionova.recipesapp.models.Recipe
import ru.marinarodionova.recipesapp.models.toDomain
import ru.marinarodionova.recipesapp.models.toEntity

class RecipesRepository(context: Context) {
    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    private val database =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "database"
        ).build()
    private val categoryDao = database.categoryDao()
    private val recipeDao = database.recipesDao()

    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    suspend fun getCategories(): List<Category>? {
        var categories: List<Category>? = null
        try {
            withContext(Dispatchers.IO) {
                categories = service.getCategories()
            }
        } catch (e: Exception) {
            Log.i("!!!!", "getCategories failed")
        }
        return categories
    }

    suspend fun getCategoriesFromCache(): List<Category> {
        return categoryDao.getCategories()
    }

    suspend fun insertCategoriesToCache(categories: List<Category>) {
        for (category in categories) {
            categoryDao.insertCategories(category)
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        var recipeList: List<Recipe>? = null
        try {
            withContext(Dispatchers.IO) {
                recipeList = service.getRecipesByCategoryId(categoryId)
            }
        } catch (e: Exception) {
            Log.i("!!!!", "getRecipesByCategoryId failed")
        }

        return recipeList
    }

    suspend fun getRecipesByCategoryIdFromCache(categoryId: Int): List<Recipe> {
        val recipeList = recipeDao.getRecipesByCategoryId(categoryId).map { it.toDomain() }
        return recipeList
    }

    suspend fun getRecipesByIdFromCache(idSet: Set<Int>): List<Recipe> {
        val recipe = recipeDao.getRecipesById(idSet).map { it.toDomain() }
        return recipe
    }

    suspend fun getRecipeByIdFromCache(recipeId: Int): Recipe? {
        return recipeDao.getRecipeById(recipeId)?.toDomain()
    }

    suspend fun insertRecipesByCategoryToCache(recipes: List<Recipe>, categoryId: Int) {
        for (recipe in recipes) {
            recipeDao.insertRecipes(recipe.toEntity(categoryId))
        }
    }

    suspend fun getCategoryByCategoryId(categoryId: Int): Category? {
        var category: Category? = null
        try {
            withContext(Dispatchers.IO) {
                category = service.getCategoryById(categoryId)
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
                recipe = service.getRecipeById(recipeId)
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
                recipes =
                    service.getRecipesByIds(idSet.joinToString(","))
            }
        } catch (e: Exception) {
            Log.i("!!!!", "getRecipesById failed")
        }
        return recipes
    }
}