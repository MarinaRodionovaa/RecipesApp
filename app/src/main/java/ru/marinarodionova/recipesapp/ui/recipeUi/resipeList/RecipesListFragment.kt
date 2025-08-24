package ru.marinarodionova.recipesapp.ui.recipeUi.resipeList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import ru.marinarodionova.recipesapp.ARG_CATEGORY_ID
import ru.marinarodionova.recipesapp.ARG_RECIPE
import ru.marinarodionova.recipesapp.R
import ru.marinarodionova.recipesapp.databinding.FragmentRecipesListBinding
import ru.marinarodionova.recipesapp.ui.recipeUi.recipe.RecipeFragment

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeListBinding must not be null")
    private val viewModel: RecipesListViewModel by viewModels()

    private var argCategoryId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        val recipesListAdapter = RecipesListAdapter(emptyList())
        binding.rvRecipe.adapter = recipesListAdapter
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.tvRecipeTitle.text = state.categoryName
            binding.ivRecipe.setImageDrawable(state.imageUrl)
            state.recipeList?.let { recipesListAdapter.setRecipeList(it) }
            recipesListAdapter.setOnItemClickListener(object :
                RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        argCategoryId = requireArguments().getInt(ARG_CATEGORY_ID)
        viewModel.loadRecipeList(argCategoryId ?: return)
        initUI()
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