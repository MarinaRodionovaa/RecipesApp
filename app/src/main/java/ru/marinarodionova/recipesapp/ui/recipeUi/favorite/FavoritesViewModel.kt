package ru.marinarodionova.recipesapp.ui.recipeUi.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.marinarodionova.recipesapp.LoadingStatus
import ru.marinarodionova.recipesapp.data.RecipesRepository
import ru.marinarodionova.recipesapp.models.Recipe
import javax.inject.Inject

data class FavoritesState(
    val recipeList: List<Recipe>? = null,
    var loadingStatus: LoadingStatus = LoadingStatus.NOT_READY
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val recipesRepository: RecipesRepository) :
    ViewModel() {
    private val _state = MutableLiveData(FavoritesState())
    val state: LiveData<FavoritesState> get() = _state

    init {
        Log.d("!!!!", "Инициализация ViewModel и обновление")
    }

    fun loadRecipeList() {
        viewModelScope.launch {
            val recipeListData = recipesRepository.getFavoritesRecipesFromCache()
            if (recipeListData.isNotEmpty()) {
                val oldState = _state.value ?: return@launch
                val categoriesState = oldState.copy(
                    recipeList = recipeListData,
                    loadingStatus = LoadingStatus.READY
                )
                _state.value = categoriesState
            }
            val recipeListServer =
                recipesRepository.getRecipesById(recipeListData.map { it.id }.toSet())
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
}