package ru.marinarodionova.recipesapp.ui.category

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.marinarodionova.recipesapp.STUB
import ru.marinarodionova.recipesapp.models.Category

data class CategoriesState(
    val categoriesList: List<Category>? = null
)

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableLiveData(CategoriesState())
    val state: LiveData<CategoriesState> get() = _state

    init {
        Log.d("!!!!", "Инициализация ViewModel и обновление")
    }

    fun loadCategories() {
        //TODO: load from network
        val oldState = _state.value ?: return
        val categoriesState = oldState.copy(
            categoriesList = STUB.getCategories()
        )
        _state.value = categoriesState
    }
}