package ru.marinarodionova.recipesapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.marinarodionova.recipesapp.databinding.FragmentRecipeBinding
import ru.marinarodionova.recipesapp.models.Recipe
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import java.io.InputStream

class RecipeFragment : Fragment() {
    var recipe: Recipe? = null
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityLearnWordBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("ARG_RECIPE", Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable<Recipe>("ARG_RECIPE")
        }
        binding.tvRecipeTitle.text = recipe?.title
        initUI()
        initRecycler()
    }

    private fun initRecycler() {
        val recipe = recipe ?: return
        val ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
        binding.rvIngredients.adapter = ingredientsAdapter
        val methodAdapter = MethodAdapter(recipe.method)
        binding.rvMethod.adapter = methodAdapter

        val context = context ?: return
        val divider = MaterialDividerItemDecoration(context, VERTICAL).apply {
            dividerColor = ContextCompat.getColor(context, R.color.line_color)
            isLastItemDecorated = false
            dividerThickness = resources.getDimensionPixelSize(R.dimen.divider_thickness)
            dividerInsetStart = resources.getDimensionPixelOffset(R.dimen.divider_start_end)
            dividerInsetEnd = resources.getDimensionPixelOffset(R.dimen.divider_start_end)
        }

        binding.rvIngredients.addItemDecoration(divider)
        binding.rvMethod.addItemDecoration(divider)
    }

    fun initUI() {
        val inputStream: InputStream? =
            recipe?.imageUrl?.let { binding.ivRecipe.context?.assets?.open(it) }
        val drawable = Drawable.createFromStream(inputStream, null)
        binding.ivRecipe.setImageDrawable(drawable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}