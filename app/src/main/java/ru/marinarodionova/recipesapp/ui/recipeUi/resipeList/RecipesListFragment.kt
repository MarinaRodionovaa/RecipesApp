package ru.marinarodionova.recipesapp.ui.recipeUi.resipeList

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.marinarodionova.recipesapp.ARG_CATEGORY_ID
import ru.marinarodionova.recipesapp.ARG_CATEGORY_IMAGE_URL
import ru.marinarodionova.recipesapp.ARG_CATEGORY_NAME
import ru.marinarodionova.recipesapp.ARG_RECIPE
import ru.marinarodionova.recipesapp.R
import ru.marinarodionova.recipesapp.STUB
import ru.marinarodionova.recipesapp.databinding.FragmentRecipesListBinding
import ru.marinarodionova.recipesapp.ui.recipeUi.recipe.RecipeFragment
import java.io.InputStream

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeListBinding must not be null")

    private var argCategoryId: Int? = null
    private var argCategoryName: String? = null
    private var argCategoryImageUrl: String? = null

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

    private fun initRecycler() {
        val categoryId = argCategoryId ?: return
        val dataSet = STUB.getRecipesByCategoryId(categoryId)
        val recipesListAdapter = RecipesListAdapter(dataSet)
        binding.rvRecipe.adapter = recipesListAdapter

        recipesListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        argCategoryId = requireArguments().getInt(ARG_CATEGORY_ID)
        argCategoryName = requireArguments().getString(ARG_CATEGORY_NAME)
        argCategoryImageUrl = requireArguments().getString(ARG_CATEGORY_IMAGE_URL)
        binding.tvRecipeTitle.text = argCategoryName

        val inputStream: InputStream? =
            argCategoryImageUrl?.let { binding.ivRecipe.context?.assets?.open(it) }
        val drawable = Drawable.createFromStream(inputStream, null)
        binding.ivRecipe.setImageDrawable(drawable)
        initRecycler()
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
}