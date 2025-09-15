package ru.marinarodionova.recipesapp.ui.recipeUi.resipeList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.marinarodionova.recipesapp.GET_IMG_API
import ru.marinarodionova.recipesapp.LoadingStatus
import ru.marinarodionova.recipesapp.data.RecipesRepository
import ru.marinarodionova.recipesapp.models.Category
import ru.marinarodionova.recipesapp.models.Recipe

data class RecipesListState(
    val imageUrl: String? = null,
    val categoryName: String? = null,
    val categoryId: Int? = null,
    val recipeList: List<Recipe>? = null,
    var loadingStatus: LoadingStatus = LoadingStatus.NOT_READY
)

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableLiveData(RecipesListState())
    val state: LiveData<RecipesListState> get() = _state
    private val recipesRepository = RecipesRepository()

    init {
        Log.d("!!!!", "Инициализация ViewModel и обновление")
    }

    fun loadRecipeList(category: Category) {
        viewModelScope.launch {
            val recipeList = recipesRepository.getRecipesByCategoryId(category.id)
            val oldState = _state.value ?: return@launch
            val categoriesState = oldState.copy(
                recipeList = recipeList,
                categoryId = category.id,
                categoryName = category.title,
                imageUrl = "$GET_IMG_API${category.imageUrl}",
                loadingStatus = if (recipeList == null) {
                    LoadingStatus.FAILED
                } else {
                    LoadingStatus.READY
                }
            )
            _state.value = categoriesState
        }
    }
}