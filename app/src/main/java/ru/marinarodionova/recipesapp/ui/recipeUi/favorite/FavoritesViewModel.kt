package ru.marinarodionova.recipesapp.ui.recipeUi.favorite

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.marinarodionova.recipesapp.FAVORITES_PREFS_NAME
import ru.marinarodionova.recipesapp.KEY_FAVORITES_SET
import ru.marinarodionova.recipesapp.LoadingStatus
import ru.marinarodionova.recipesapp.data.RecipesRepository
import ru.marinarodionova.recipesapp.models.Recipe

data class FavoritesState(
    val recipeList: List<Recipe>? = null,
    var loadingStatus: LoadingStatus = LoadingStatus.NOT_READY
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableLiveData(FavoritesState())
    val state: LiveData<FavoritesState> get() = _state
    private val recipesRepository = RecipesRepository()

    init {
        Log.d("!!!!", "Инициализация ViewModel и обновление")
    }

    fun loadRecipeList() {
        viewModelScope.launch {
            val recipeList =
                recipesRepository.getRecipesById(getFavorites().map { it.toInt() }.toSet())
            val oldState = _state.value ?: return@launch
            val categoriesState = oldState.copy(
                recipeList = recipeList,
                loadingStatus = if (recipeList == null) {
                    LoadingStatus.FAILED
                } else {
                    LoadingStatus.READY
                }
            )

            _state.value = categoriesState
        }
    }

    private fun getFavorites(): HashSet<String> {
        val sharedPrefs =
            getApplication<Application>().getSharedPreferences(
                FAVORITES_PREFS_NAME,
                Context.MODE_PRIVATE
            )
        return HashSet(sharedPrefs.getStringSet(KEY_FAVORITES_SET, emptySet()) ?: emptySet())
    }
}