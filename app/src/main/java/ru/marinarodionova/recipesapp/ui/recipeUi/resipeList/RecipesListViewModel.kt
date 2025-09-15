package ru.marinarodionova.recipesapp.ui.recipeUi.resipeList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.marinarodionova.recipesapp.GET_IMG_API
import ru.marinarodionova.recipesapp.data.RecipesRepository
import ru.marinarodionova.recipesapp.models.Category
import ru.marinarodionova.recipesapp.models.Recipe

data class RecipesListState(
    val imageUrl: String? = null,
    val categoryName: String? = null,
    val categoryId: Int? = null,
    val recipeList: List<Recipe>? = null
)

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableLiveData(RecipesListState())
    val state: LiveData<RecipesListState> get() = _state
    private val recipesRepository = RecipesRepository()

    init {
        Log.d("!!!!", "Инициализация ViewModel и обновление")
    }

    fun loadRecipeList(category: Category) {
        val recipeList = recipesRepository.getRecipesByCategoryId(category.id)
        val oldState = _state.value ?: return
        val categoriesState = oldState.copy(
            recipeList = recipeList,
            categoryId = category.id,
            categoryName = category.title,
            imageUrl = "$GET_IMG_API${category.imageUrl}"
        )

        _state.value = categoriesState
    }
}