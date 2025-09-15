package ru.marinarodionova.recipesapp.ui.recipeUi.resipeList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import ru.marinarodionova.recipesapp.LoadingStatus
import ru.marinarodionova.recipesapp.R
import ru.marinarodionova.recipesapp.databinding.FragmentRecipesListBinding
import ru.marinarodionova.recipesapp.models.Category

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeListBinding must not be null")
    private val viewModel: RecipesListViewModel by viewModels()
    private val args: RecipesListFragmentArgs by navArgs()
    private var argCategory: Category? = null

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
            Glide.with(this).load(state.imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivRecipe)
            state.recipeList?.let {
                recipesListAdapter.setRecipeList(it)
            }
            if (state.loadingStatus == LoadingStatus.FAILED) {
                Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            }
            recipesListAdapter.setOnItemClickListener(object :
                RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        argCategory = args.category
        viewModel.loadRecipeList(argCategory ?: return)
        initUI()
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        val action = RecipesListFragmentDirections.recipeAction(recipeId)
        findNavController().navigate(action)
    }
}