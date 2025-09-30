package ru.marinarodionova.recipesapp.ui.recipeUi.resipeList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.marinarodionova.recipesapp.GET_IMG_API
import ru.marinarodionova.recipesapp.LoadingStatus
import ru.marinarodionova.recipesapp.data.RecipesRepository
import ru.marinarodionova.recipesapp.models.Category
import ru.marinarodionova.recipesapp.models.Recipe
import javax.inject.Inject

data class RecipesListState(
    val imageUrl: String? = null,
    val categoryName: String? = null,
    val categoryId: Int? = null,
    val recipeList: List<Recipe>? = null,
    var loadingStatus: LoadingStatus = LoadingStatus.NOT_READY
)

@HiltViewModel
class RecipesListViewModel @Inject constructor(private val recipesRepository: RecipesRepository) :
    ViewModel() {
    private val _state = MutableLiveData(RecipesListState())
    val state: LiveData<RecipesListState> get() = _state

    init {
        Log.d("!!!!", "Инициализация ViewModel и обновление")
    }

    fun loadRecipeList(category: Category) {
        viewModelScope.launch {
            val recipeListFromCache = recipesRepository.getRecipesByCategoryIdFromCache(category.id)
            if (recipeListFromCache.isNotEmpty()) {
                val oldState = _state.value ?: return@launch
                val recipesState = oldState.copy(
                    recipeList = recipeListFromCache,
                    categoryId = category.id,
                    categoryName = category.title,
                    imageUrl = "$GET_IMG_API${category.imageUrl}",
                    loadingStatus = LoadingStatus.READY
                )
                _state.value = recipesState
            }
            val recipeList = recipesRepository.getRecipesByCategoryId(category.id)
            if (recipeListFromCache != recipeList && recipeList != null) {
                val oldState = _state.value ?: return@launch
                val recipesState = oldState.copy(
                    recipeList = recipeList,
                    categoryId = category.id,
                    categoryName = category.title,
                    imageUrl = "$GET_IMG_API${category.imageUrl}",
                    loadingStatus = LoadingStatus.READY
                )
                _state.value = recipesState

                recipesRepository.insertRecipesByCategoryToCache(recipeList, category.id)
            } else if (recipeList == null) {
                val oldState = _state.value ?: return@launch
                val recipesState = oldState.copy(
                    loadingStatus = LoadingStatus.FAILED
                )
                _state.value = recipesState
            }
        }
    }
}