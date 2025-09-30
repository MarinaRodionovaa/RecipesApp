package ru.marinarodionova.recipesapp.ui.recipeUi.recipe

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
import ru.marinarodionova.recipesapp.models.Ingredient
import ru.marinarodionova.recipesapp.models.toDomain
import javax.inject.Inject

data class RecipeState(
    val recipeId: Int? = null,
    val recipeName: String? = null,
    val recipeImg: String? = null,
    val method: List<String?> = listOf(null),
    val ingredientList: List<Ingredient?> = listOf(null),
    val portionCount: Int = 1,
    val isFavorite: Boolean? = null,
    val imageUrl: String? = null,
    var loadingStatus: LoadingStatus = LoadingStatus.NOT_READY
)

@HiltViewModel
class RecipeViewModel @Inject constructor(private val recipesRepository: RecipesRepository) :
    ViewModel() {
    private var favoritesSet: Set<Int>? = null
    private val _state = MutableLiveData(RecipeState())
    private val currentPortionCount = _state.value?.portionCount ?: 1
    val state: LiveData<RecipeState> get() = _state

    init {
        Log.d("!!!!", "Инициализация RecipeViewModel и обновление")
        _state.value = RecipeState(isFavorite = true)
    }

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            favoritesSet = getFavorites()
            val isRecipeInSet = recipeId in favoritesSet.orEmpty()
            val recipeData = recipesRepository.getRecipeByIdFromCache(recipeId)
            if (recipeData != null) {
                val oldState = _state.value ?: return@launch
                val recipeState = oldState.copy(
                    recipeId = recipeId,
                    recipeName = recipeData.title,
                    method = recipeData.method,
                    ingredientList = recipeData.ingredients,
                    portionCount = currentPortionCount,
                    isFavorite = isRecipeInSet,
                    imageUrl = recipeData.imageUrl,
                    recipeImg = "$GET_IMG_API${recipeData.imageUrl}",
                    loadingStatus = LoadingStatus.READY
                )
                _state.value = recipeState
            }
            val recipeFromServer = recipesRepository.getRecipeById(recipeId)
            if (recipeData?.toDomain() != recipeFromServer && recipeFromServer != null) {
                val oldState = _state.value ?: return@launch
                val recipeState = oldState.copy(
                    recipeId = recipeId,
                    recipeName = recipeFromServer.title,
                    method = recipeFromServer.method,
                    ingredientList = recipeFromServer.ingredients,
                    portionCount = currentPortionCount,
                    isFavorite = isRecipeInSet,
                    recipeImg = "$GET_IMG_API${recipeFromServer.imageUrl}",
                    loadingStatus = LoadingStatus.READY,
                    imageUrl = recipeFromServer.imageUrl,
                )
                _state.value = recipeState

            } else if (recipeFromServer == null) {
                val oldState = _state.value ?: return@launch
                val recipeState = oldState.copy(
                    loadingStatus = LoadingStatus.FAILED
                )
                _state.value = recipeState
            }
        }
    }

    private suspend fun getFavorites(): Set<Int> {
        return recipesRepository.getFavoritesIdsFromCache()
    }

    fun onFavoritesClicked(recipeId: Int) {
        viewModelScope.launch {
            val recipe = recipesRepository.getRecipeByIdFromCache(recipeId)
            if (recipe?.isFavorite == true) {
                val oldState = _state.value ?: return@launch
                val recipeState = oldState.copy(
                    isFavorite = false
                )
                _state.value = recipeState
                recipesRepository.insertRecipeToCache(recipe.copy(isFavorite = false))
            } else if (recipe?.isFavorite == false) {
                val oldState = _state.value ?: return@launch
                val recipeState = oldState.copy(
                    isFavorite = true
                )
                _state.value = recipeState
                recipesRepository.insertRecipeToCache(recipe.copy(isFavorite = true))
            }
        }
    }

    fun updatePortionCount(count: Int) {
        val oldState = _state.value ?: return
        val recipeState = oldState.copy(portionCount = count)
        _state.value = recipeState
    }
}