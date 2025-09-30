package ru.marinarodionova.recipesapp.di

import ru.marinarodionova.recipesapp.data.RecipesRepository
import ru.marinarodionova.recipesapp.ui.category.CategoriesListViewModel

class CategoriesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<CategoriesListViewModel> {
    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(recipesRepository)
    }
}