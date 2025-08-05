package ru.marinarodionova.recipesapp.ui.recipeUi.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import ru.marinarodionova.recipesapp.FAVORITES_PREFS_NAME
import ru.marinarodionova.recipesapp.KEY_FAVORITES_SET
import ru.marinarodionova.recipesapp.STUB
import ru.marinarodionova.recipesapp.models.Ingredient

data class RecipeState(
    var recipeId: Int? = null,
    var recipeName: String? = null,
    var recipeImg: String? = null,
    var method: List<String?> = listOf(null),
    var ingredientList: List<Ingredient?> = listOf(null),
    var portionCount: Int = 1,
    var isFavorite: Boolean? = null
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private var favoritesSet: HashSet<String>? = null
    private val _state = MutableLiveData<RecipeState>(RecipeState())
    val currentPortionCount = _state.value?.portionCount ?: 1
    val state: LiveData<RecipeState> get() = _state

    init {
        Log.d("!!!!", "Инициализация ViewModel и обновление")
        _state.value = RecipeState(isFavorite = true)
    }

    fun loadRecipe(recipeId: Int) {
//        TODO: load from network
        val recipe = STUB.getRecipeById(recipeId)
        favoritesSet = getFavorites()
        val isRecipeInSet = recipeId.toString() in favoritesSet.orEmpty()
        val recipeState = recipe?.method?.let {
            RecipeState(
                recipeId,
                recipe.title,
                recipe.imageUrl,
                it,
                recipe.ingredients,
                currentPortionCount,
                isRecipeInSet
            )
        }
        recipeState?.let {
            _state.value = it
        }
    }

    fun getFavorites(): HashSet<String> {
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

    fun saveFavorites(favoritesSet: Set<String>) {
        val sharedPrefs =
            getApplication<Application>().getSharedPreferences(
                FAVORITES_PREFS_NAME,
                Context.MODE_PRIVATE
            )
        sharedPrefs?.edit {
            putStringSet(KEY_FAVORITES_SET, favoritesSet)
        }
    }
}