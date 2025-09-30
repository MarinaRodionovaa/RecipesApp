package ru.marinarodionova.recipesapp.di

import ru.marinarodionova.recipesapp.data.RecipesRepository
import ru.marinarodionova.recipesapp.ui.recipeUi.favorite.FavoritesViewModel

class FavoritesViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<FavoritesViewModel> {
    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository)
    }
}