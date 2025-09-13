package ru.marinarodionova.recipesapp.ui.recipeUi.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import ru.marinarodionova.recipesapp.FAVORITES_PREFS_NAME
import ru.marinarodionova.recipesapp.GET_IMG_API
import ru.marinarodionova.recipesapp.KEY_FAVORITES_SET
import ru.marinarodionova.recipesapp.data.RecipesRepository
import ru.marinarodionova.recipesapp.models.Ingredient

data class RecipeState(
    val recipeId: Int? = null,
    val recipeName: String? = null,
    val recipeImg: String? = null,
    val method: List<String?> = listOf(null),
    val ingredientList: List<Ingredient?> = listOf(null),
    val portionCount: Int = 1,
    val isFavorite: Boolean? = null
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private var favoritesSet: HashSet<String>? = null
    private val _state = MutableLiveData(RecipeState())
    private val currentPortionCount = _state.value?.portionCount ?: 1
    val state: LiveData<RecipeState> get() = _state
    private val recipesRepository = RecipesRepository()

    init {
        Log.d("!!!!", "Инициализация RecipeViewModel и обновление")
        _state.value = RecipeState(isFavorite = true)
    }

    fun loadRecipe(recipeId: Int) {
        val recipe = recipesRepository.getRecipeById(recipeId)
        favoritesSet = getFavorites()
        val isRecipeInSet = recipeId.toString() in favoritesSet.orEmpty()
        val oldState = _state.value ?: return
        val recipeState = oldState.copy(
            recipeId = recipeId,
            recipeName = recipe?.title,
            method = recipe?.method ?: listOf(null),
            ingredientList = recipe?.ingredients ?: listOf(null),
            portionCount = currentPortionCount,
            isFavorite = isRecipeInSet,
            recipeImg = "$GET_IMG_API${recipe?.imageUrl}"
        )
        _state.value = recipeState
    }

    private fun getFavorites(): HashSet<String> {
        val sharedPrefs =
            getApplication<Application>().getSharedPreferences(
                FAVORITES_PREFS_NAME,
                Context.MODE_PRIVATE
            )
        return HashSet(sharedPrefs.getStringSet(KEY_FAVORITES_SET, emptySet()) ?: emptySet())
    }

    fun onFavoritesClicked() {
        val isFavorite = state.value?.isFavorite ?: return
        if (isFavorite) {
            favoritesSet?.remove(state.value?.recipeId.toString())
        } else {
            favoritesSet?.add(state.value?.recipeId.toString())
        }
        _state.value = state.value?.copy(isFavorite = !isFavorite)
        saveFavorites(favoritesSet?.toSet() ?: emptySet())
        favoritesSet = getFavorites()
    }

    private fun saveFavorites(favoritesSet: Set<String>) {
        val sharedPrefs =
            getApplication<Application>().getSharedPreferences(
                FAVORITES_PREFS_NAME,
                Context.MODE_PRIVATE
            )
        sharedPrefs?.edit {
            putStringSet(KEY_FAVORITES_SET, favoritesSet)
        }
    }

    fun updatePortionCount(count: Int) {
        val oldState = _state.value ?: return
        val recipeState = oldState.copy(portionCount = count)
        _state.value = recipeState
    }
}