package ru.marinarodionova.recipesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.marinarodionova.recipesapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityLearnWordBinding must not be null")

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        argCategoryId = requireArguments().getInt("ARG_CATEGORY_ID")
        argCategoryName = requireArguments().getString("ARG_CATEGORY_NAME")
        argCategoryImageUrl = requireArguments().getString("ARG_CATEGORY_IMAGE_URL")

    }
}