package ru.marinarodionova.recipesapp.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import ru.marinarodionova.recipesapp.LoadingStatus
import ru.marinarodionova.recipesapp.RecipesApplication
import ru.marinarodionova.recipesapp.databinding.FragmentCategoriesListBinding
import ru.marinarodionova.recipesapp.models.Category

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentCategoriesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentCategoriesListBinding must not be null")
    private lateinit var viewModel: CategoriesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        viewModel = appContainer.categoriesListViewModelFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadCategories()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        val categoriesListAdapter = CategoriesListAdapter(emptyList())
        binding.rvCategories.adapter = categoriesListAdapter
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state.loadingStatus) {
                LoadingStatus.READY -> state.categoriesList?.let {
                    categoriesListAdapter.setCategories(
                        it
                    )
                }

                LoadingStatus.FAILED -> {
                    Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
                }

                LoadingStatus.NOT_READY -> {}
            }
        }
        categoriesListAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(category: Category) {
                openRecipesByCategoryId(category)
            }
        })
    }

    fun openRecipesByCategoryId(category: Category) {
        val action = CategoriesListFragmentDirections.recipeListAction(category)
        findNavController().navigate(action)
    }
}