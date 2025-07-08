package ru.marinarodionova.recipesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.marinarodionova.recipesapp.databinding.FragmentCategoriesListBinding

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentCategoriesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentCategoriesListBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val categoriesListAdapter = CategoriesListAdapter(STUB.getCategories())
        binding.rvCategories.adapter = categoriesListAdapter

        categoriesListAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    fun openRecipesByCategoryId(categoryId: Int) {
        val categoryName = STUB.getCategories()[categoryId].title
        val categoryImageUrl = STUB.getCategories()[categoryId].imageUrl
        val bundle = Bundle().apply {
            putInt("ARG_CATEGORY_ID", categoryId)
            putString("ARG_CATEGORY_NAME", categoryName)
            putString("ARG_CATEGORY_IMAGE_URL", categoryImageUrl)
        }
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipesListFragment>(R.id.mainFragmentContainer, args = bundle)
        }
    }
}