package ru.marinarodionova.recipesapp.ui.recipeUi.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.marinarodionova.recipesapp.LoadingStatus
import ru.marinarodionova.recipesapp.databinding.FragmentFavoritesBinding
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
            state.recipeList?.let { recipesListAdapter.setRecipeList(it) }

            if (state.loadingStatus != LoadingStatus.NOT_READY && state.recipeList == null) {
                Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
                binding.clInformationMessage.visibility = View.VISIBLE
            }

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
        val action = FavoritesFragmentDirections.favoritesFragmentAction(recipeId)
        findNavController().navigate(action)
    }
}