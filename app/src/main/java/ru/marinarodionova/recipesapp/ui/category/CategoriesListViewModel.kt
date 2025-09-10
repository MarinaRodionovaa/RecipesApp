package ru.marinarodionova.recipesapp.ui.category

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.marinarodionova.recipesapp.data.RecipesRepository
import ru.marinarodionova.recipesapp.models.Category

data class CategoriesState(
    val categoriesList: List<Category>? = null
)

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableLiveData(CategoriesState())
    val state: LiveData<CategoriesState> get() = _state
    private val recipesRepository = RecipesRepository()

    init {
        Log.d("!!!!", "Инициализация ViewModel и обновление")
    }

    fun loadCategories() {
        val categoriesList = recipesRepository.getCategories()
        val oldState = _state.value ?: return
        val categoriesState = oldState.copy(
            categoriesList = categoriesList
        )
        _state.value = categoriesState
    }
}