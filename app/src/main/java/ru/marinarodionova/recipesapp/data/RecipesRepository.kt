package ru.marinarodionova.recipesapp.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.marinarodionova.recipesapp.models.Category
import ru.marinarodionova.recipesapp.models.Recipe
import ru.marinarodionova.recipesapp.models.RecipeEntity
import ru.marinarodionova.recipesapp.models.toDomain
import ru.marinarodionova.recipesapp.models.toEntity
import javax.inject.Inject

class RecipesRepository @Inject constructor(
    private val recipeDao: RecipesDao,
    private val categoryDao: CategoriesDao,
    private val service: RecipeApiService
) {
    private val ioDispatcher = Dispatchers.IO
    suspend fun getCategories(): List<Category>? {
        var categories: List<Category>? = null
        try {
            withContext(ioDispatcher) {
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
            withContext(ioDispatcher) {
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

    suspend fun getRecipeByIdFromCache(recipeId: Int): RecipeEntity? {
        return recipeDao.getRecipeById(recipeId)
    }

    suspend fun insertRecipesByCategoryToCache(recipes: List<Recipe>, categoryId: Int) {
        for (recipe in recipes) {
            val isFavorite = recipe.id in recipeDao.getFavoritesIds()
            recipeDao.insertRecipes(recipe.toEntity(categoryId, isFavorite))
        }
    }

    suspend fun insertRecipeToCache(recipe: RecipeEntity) {
        recipeDao.insertRecipes(recipe)
    }

    suspend fun getFavoritesIdsFromCache(): Set<Int> {
        return recipeDao.getFavoritesIds().toSet()
    }

    suspend fun getFavoritesRecipesFromCache(): List<Recipe> {
        return recipeDao.getFavoritesRecipes().map { it.toDomain() }
    }

    suspend fun getCategoryByCategoryId(categoryId: Int): Category? {
        var category: Category? = null
        try {
            withContext(ioDispatcher) {
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
            withContext(ioDispatcher) {
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
            withContext(ioDispatcher) {
                recipes =
                    service.getRecipesByIds(idSet.joinToString(","))
            }
        } catch (e: Exception) {
            Log.i("!!!!", "getRecipesById failed")
        }
        return recipes
    }
}