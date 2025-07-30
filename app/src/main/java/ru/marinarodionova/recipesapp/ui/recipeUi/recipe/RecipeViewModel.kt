package ru.marinarodionova.recipesapp.ui.recipeUi.recipe

import androidx.lifecycle.ViewModel
import ru.marinarodionova.recipesapp.models.Ingredient
import ru.marinarodionova.recipesapp.models.Recipe

data class RecipeState(
    var recipeName: String? = null,
    var recipeImg: String? = null,
    var recipe: Recipe? = null,
    var method: List<String?> = listOf(null),
    var ingredientList: List<Ingredient?> = listOf(null),
    var portionCount: Int = 1
)

class RecipeViewModel : ViewModel() {
}