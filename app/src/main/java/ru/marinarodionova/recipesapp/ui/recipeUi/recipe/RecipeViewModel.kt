package ru.marinarodionova.recipesapp.ui.recipeUi.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.marinarodionova.recipesapp.models.Ingredient
import ru.marinarodionova.recipesapp.models.Recipe

data class RecipeState(
    var recipeName: String? = null,
    var recipeImg: String? = null,
    var recipe: Recipe? = null,
    var method: List<String?> = listOf(null),
    var ingredientList: List<Ingredient?> = listOf(null),
    var portionCount: Int = 1,
    var isFavorite: Boolean? = null
)

class RecipeViewModel : ViewModel() {
    private val _state = MutableLiveData<RecipeState>(RecipeState())
    val state: LiveData<RecipeState> get() = _state

    init {
        Log.d("!!!!", "Инициализация ViewModel и обновление")
        _state.value = RecipeState(isFavorite = true)
    }
}