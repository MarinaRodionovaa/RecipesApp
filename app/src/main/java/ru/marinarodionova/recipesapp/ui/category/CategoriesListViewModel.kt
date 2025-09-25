package ru.marinarodionova.recipesapp.ui.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.marinarodionova.recipesapp.LoadingStatus
import ru.marinarodionova.recipesapp.data.RecipesRepository
import ru.marinarodionova.recipesapp.models.Category

data class CategoriesState(
    val categoriesList: List<Category>? = null,
    var loadingStatus: LoadingStatus = LoadingStatus.NOT_READY
)

class CategoriesListViewModel(
    private val recipesRepository: RecipesRepository
) : ViewModel() {
    private val _state = MutableLiveData(CategoriesState())
    val state: LiveData<CategoriesState> get() = _state

    init {
        Log.d("!!!!", "Инициализация ViewModel и обновление")
    }

    fun loadCategories() {
        viewModelScope.launch {
            val categoriesListFormCache = recipesRepository.getCategoriesFromCache()
            if (categoriesListFormCache.isNotEmpty()) {
                val oldState = _state.value ?: return@launch
                val categoriesState = oldState.copy(
                    categoriesList = categoriesListFormCache,
                    loadingStatus = if (categoriesListFormCache.isEmpty()) {
                        LoadingStatus.NOT_READY
                    } else {
                        LoadingStatus.READY
                    }
                )
                _state.value = categoriesState
            }
            val categoriesList = recipesRepository.getCategories()
            if (categoriesListFormCache != categoriesList && categoriesList != null) {
                val oldState = _state.value ?: return@launch
                val categoriesState = oldState.copy(
                    categoriesList = categoriesList,
                    loadingStatus = LoadingStatus.READY
                )
                _state.value = categoriesState
                recipesRepository.insertCategoriesToCache(categoriesList)
            } else if (categoriesList == null) {
                val oldState = _state.value ?: return@launch
                val categoriesState = oldState.copy(
                    loadingStatus = LoadingStatus.FAILED
                )
                _state.value = categoriesState
            }
        }
    }
}