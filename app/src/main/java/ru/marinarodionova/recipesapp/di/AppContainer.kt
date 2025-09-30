package ru.marinarodionova.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.marinarodionova.recipesapp.data.AppDatabase
import ru.marinarodionova.recipesapp.data.RecipeApiService
import ru.marinarodionova.recipesapp.data.RecipesRepository

class AppContainer(context: Context) {
    private val database =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "database"
        ).fallbackToDestructiveMigration(true).build()

    private val categoryDao = database.categoryDao()
    private val recipeDao = database.recipesDao()
    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    private val recipeApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)
    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
    private val ioDispatcher = Dispatchers.IO
    val repository =
        RecipesRepository(recipeDao, categoryDao, service, ioDispatcher, recipeApiService)

    val categoriesListViewModelFactory = CategoriesListViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repository)
}