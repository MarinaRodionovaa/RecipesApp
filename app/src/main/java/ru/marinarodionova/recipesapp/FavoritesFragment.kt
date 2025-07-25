package ru.marinarodionova.recipesapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.marinarodionova.recipesapp.databinding.FragmentFavoritesBinding
import ru.marinarodionova.recipesapp.models.Recipe

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoriteBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()
    }

    private fun initRecycler() {
        val favoritesList = getFavorites()
        val dataSet: List<Recipe> = STUB.getRecipesById(favoritesList)
        if (dataSet.isEmpty()) {
            binding.clInformationMessage.visibility = View.VISIBLE
        } else {
            val recipesListAdapter = RecipesListAdapter(dataSet)
            binding.rvRecipe.adapter = recipesListAdapter

            recipesListAdapter.setOnItemClickListener(object :
                RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            }
            )
        }
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val bundle = Bundle().apply {
            putParcelable(ARG_RECIPE, recipe)
        }
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainFragmentContainer, args = bundle)
        }
    }

    fun getFavorites(): HashSet<Int> {
        val sharedPrefs =
            requireContext().getSharedPreferences(FAVORITES_PREFS_NAME, Context.MODE_PRIVATE)
        val stringSet = sharedPrefs.getStringSet(KEY_FAVORITES_SET, emptySet()) ?: emptySet()
        return stringSet.mapNotNull { it.toIntOrNull() }.toHashSet()
    }
}