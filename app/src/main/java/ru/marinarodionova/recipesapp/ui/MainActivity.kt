package ru.marinarodionova.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import kotlinx.serialization.json.Json
import ru.marinarodionova.recipesapp.R
import ru.marinarodionova.recipesapp.databinding.ActivityMainBinding
import java.util.concurrent.Executors
import ru.marinarodionova.recipesapp.models.Category
import ru.marinarodionova.recipesapp.models.Recipe
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Callable

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for MainActivityBinding must not be null")
    val executor = Executors.newFixedThreadPool(10)
    var categoryIdList: List<Int> = listOf()
    var recipesList: List<List<Recipe>> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("!!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().getName()}")
        _binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        val getCategories = executor.submit(Callable {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.content
            val categories = connection.inputStream.bufferedReader().readText()
            connection.disconnect()
            Log.i("!!!!", "Body: $categories")
            Log.i("!!!!", "Выполняю запрос на потоке: ${Thread.currentThread().getName()}")
            val categoryList: List<Category> = Json.decodeFromString(categories)
            categoryIdList = categoryList.map { it.id }
        })

        getCategories.get()
        executor.execute {
            recipesList = categoryIdList.map {
                val urlRecipe = URL("https://recipes.androidsprint.ru/api/category/${it}/recipes")
                val connectionRecipe = urlRecipe.openConnection() as HttpURLConnection
                val recipes = connectionRecipe.inputStream.bufferedReader().readText()
                val recipesListJson: List<Recipe> = Json.decodeFromString(recipes)
                connectionRecipe.disconnect()
                recipesListJson
            }
            Log.i("!!!!", "recipesList: $recipesList")
        }
        executor.shutdown()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnCategory.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
        }

        binding.btnFavorite.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}