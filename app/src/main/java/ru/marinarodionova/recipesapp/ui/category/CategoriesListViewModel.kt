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
    private val recipesRepository = RecipesRepository(application)

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