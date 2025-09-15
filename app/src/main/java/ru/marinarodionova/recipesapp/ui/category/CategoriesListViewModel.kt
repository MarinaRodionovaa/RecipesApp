package ru.marinarodionova.recipesapp.ui.category

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.marinarodionova.recipesapp.LoadingStatus
import ru.marinarodionova.recipesapp.data.RecipesRepository
import ru.marinarodionova.recipesapp.models.Category

data class CategoriesState(
    val categoriesList: List<Category>? = null,
    var loadingStatus: LoadingStatus = LoadingStatus.NOT_READY
)

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableLiveData(CategoriesState())
    val state: LiveData<CategoriesState> get() = _state
    private val recipesRepository = RecipesRepository()

    init {
        Log.d("!!!!", "Инициализация ViewModel и обновление")
    }

    fun loadCategories() {
        viewModelScope.launch {
            val categoriesList = recipesRepository.getCategories()
            val oldState = _state.value ?: return@launch
            val categoriesState = oldState.copy(
                categoriesList = categoriesList,
                loadingStatus = if (categoriesList == null) {
                    LoadingStatus.FAILED
                } else {
                    LoadingStatus.READY
                }
            )
            _state.value = categoriesState
        }
    }
}