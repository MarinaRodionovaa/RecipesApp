package ru.marinarodionova.recipesapp.di

import ru.marinarodionova.recipesapp.data.RecipesRepository
import ru.marinarodionova.recipesapp.ui.recipeUi.resipeList.RecipesListViewModel

class RecipesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<RecipesListViewModel> {
    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }
}