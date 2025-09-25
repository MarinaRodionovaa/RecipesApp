package ru.marinarodionova.recipesapp.di

import ru.marinarodionova.recipesapp.data.RecipesRepository
import ru.marinarodionova.recipesapp.ui.recipeUi.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }
}