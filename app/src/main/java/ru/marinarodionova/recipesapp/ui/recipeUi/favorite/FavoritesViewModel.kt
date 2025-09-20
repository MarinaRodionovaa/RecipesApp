package ru.marinarodionova.recipesapp.ui.recipeUi.favorite

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.marinarodionova.recipesapp.LoadingStatus
import ru.marinarodionova.recipesapp.data.RecipesRepository
import ru.marinarodionova.recipesapp.models.Recipe

data class FavoritesState(
    val recipeList: List<Recipe>? = null,
    var loadingStatus: LoadingStatus = LoadingStatus.NOT_READY
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableLiveData(FavoritesState())
    val state: LiveData<FavoritesState> get() = _state
    private val recipesRepository = RecipesRepository(application)

    init {
        Log.d("!!!!", "Инициализация ViewModel и обновление")
    }

    fun loadRecipeList() {
        viewModelScope.launch {
            val favorites = getFavorites()
            val recipeListData = recipesRepository.getRecipesByIdFromCache(favorites)
            if (recipeListData.isNotEmpty()) {
                val oldState = _state.value ?: return@launch
                val categoriesState = oldState.copy(
                    recipeList = recipeListData,
                    loadingStatus = LoadingStatus.READY
                )
                _state.value = categoriesState
            }
            val recipeListServer =
                recipesRepository.getRecipesById(favorites)
            if (recipeListServer != recipeListData && recipeListServer != null) {
                val oldState = _state.value ?: return@launch
                val categoriesState = oldState.copy(
                    recipeList = recipeListServer,
                    loadingStatus = LoadingStatus.READY
                )
                _state.value = categoriesState
            } else if (recipeListServer == null) {
                val oldState = _state.value ?: return@launch
                val categoriesState = oldState.copy(
                    loadingStatus = LoadingStatus.FAILED
                )
                _state.value = categoriesState
            }
        }
    }

    private suspend fun getFavorites(): Set<Int> {
        return recipesRepository.getFavoritesFromCache()
    }
}