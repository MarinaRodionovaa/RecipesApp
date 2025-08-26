package ru.marinarodionova.recipesapp.ui.recipeUi.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import ru.marinarodionova.recipesapp.ARG_RECIPE
import ru.marinarodionova.recipesapp.R
import ru.marinarodionova.recipesapp.databinding.FragmentFavoritesBinding
import ru.marinarodionova.recipesapp.models.Recipe
import ru.marinarodionova.recipesapp.ui.recipeUi.recipe.RecipeFragment
import ru.marinarodionova.recipesapp.ui.recipeUi.resipeList.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoriteBinding must not be null")
    private val viewModel: FavoritesViewModel by viewModels()

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
        viewModel.loadRecipeList()
        initUI()
    }

    private fun initUI() {
        val recipesListAdapter = RecipesListAdapter(emptyList())
        binding.rvRecipe.adapter = recipesListAdapter
        viewModel.state.observe(viewLifecycleOwner) { state ->
            val dataSet: List<Recipe>? = state.recipeList
            if (dataSet == null) {
                binding.clInformationMessage.visibility = View.VISIBLE
            } else {
                recipesListAdapter.setRecipeList(dataSet)
                recipesListAdapter.setOnItemClickListener(object :
                    RecipesListAdapter.OnItemClickListener {
                    override fun onItemClick(recipeId: Int) {
                        openRecipeByRecipeId(recipeId)
                    }
                }
                )
            }
        }
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = Bundle().apply {
            putInt(ARG_RECIPE, recipeId)
        }
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainFragmentContainer, args = bundle)
        }
    }
}