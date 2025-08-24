package ru.marinarodionova.recipesapp.ui.recipeUi.resipeList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.marinarodionova.recipesapp.STUB
import ru.marinarodionova.recipesapp.models.Recipe

data class RecipesListState(
    val imageUrl: Drawable? = null,
    val categoryName: String? = null,
    val categoryId: Int? = null,
    val recipeList: List<Recipe>? = null
)

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableLiveData(RecipesListState())
    val state: LiveData<RecipesListState> get() = _state

    init {
        Log.d("!!!!", "Инициализация ViewModel и обновление")
    }

    fun loadRecipeList(categoryId: Int) {
        //TODO: load from network
        val category = STUB.getCategoryByCategoryId(categoryId)
        val recipeList = STUB.getRecipesByCategoryId(categoryId)
        val oldState = _state.value ?: return
        val categoriesState = oldState.copy(
            recipeList = recipeList,
            categoryId = category?.id,
            categoryName = category?.title,
            imageUrl = try {
                category?.imageUrl?.let { img ->
                    getApplication<Application>().assets.open(img).use { inputStream ->
                        Drawable.createFromStream(inputStream, null)
                    }
                }
            } catch (e: Exception) {
                Log.d("!!!", "Image not found ${category?.imageUrl}")
                null
            }
        )

        _state.value = categoriesState
    }
}