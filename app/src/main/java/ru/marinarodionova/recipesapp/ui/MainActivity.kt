package ru.marinarodionova.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import ru.marinarodionova.recipesapp.R
import ru.marinarodionova.recipesapp.databinding.ActivityMainBinding
import ru.marinarodionova.recipesapp.models.Category
import ru.marinarodionova.recipesapp.models.Recipe
import java.util.concurrent.Callable
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for MainActivityBinding must not be null")
    private val executor = Executors.newFixedThreadPool(10)
    private var categoryIdList: List<Int>? = listOf()
    private var recipesList: List<List<Recipe>?>? = listOf()
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder().addInterceptor(logging).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val getCategories = executor.submit(Callable {
            val categories: String?
            val request =
                Request.Builder().url("https://recipes.androidsprint.ru/api/category").build()
            client.newCall(request).execute().use { response ->
                categories = response.body?.string()
                Log.i("!!!!", "response: $response")
                response.close()
            }
            Log.i("!!!!", "Body: $categories")
            Log.i("!!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

            val categoryList: List<Category>? = categories?.let { Json.decodeFromString(it) }
            categoryIdList = categoryList?.map { it.id }
        })

        getCategories.get()
        executor.execute {
            var recipes: String?
            recipesList = categoryIdList?.map {
                val requestRecipe = Request.Builder()
                    .url("https://recipes.androidsprint.ru/api/category/${it}/recipes").build()
                client.newCall(requestRecipe).execute().use { response ->
                    recipes = response.body?.string()
                    Log.i("!!!!", "response: $response")
                    response.close()
                }
                val recipesListJson: List<Recipe>? =
                    recipes?.let { Json.decodeFromString(it) }
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