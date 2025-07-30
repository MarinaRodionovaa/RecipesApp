package ru.marinarodionova.recipesapp.ui.recipeUi.recipe

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.marinarodionova.recipesapp.databinding.FragmentRecipeBinding
import ru.marinarodionova.recipesapp.models.Recipe
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import java.io.InputStream
import androidx.core.content.edit
import ru.marinarodionova.recipesapp.ARG_RECIPE
import ru.marinarodionova.recipesapp.FAVORITES_PREFS_NAME
import ru.marinarodionova.recipesapp.KEY_FAVORITES_SET
import ru.marinarodionova.recipesapp.R

class RecipeFragment : Fragment() {
    private var recipe: Recipe? = null
    private var favoritesSet: HashSet<String>? = null
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")

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
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable<Recipe>(ARG_RECIPE)
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
            dividerInsetStart = resources.getDimensionPixelOffset(R.dimen.medium_padding)
            dividerInsetEnd = resources.getDimensionPixelOffset(R.dimen.medium_padding)
        }

        binding.rvIngredients.addItemDecoration(divider)
        binding.rvMethod.addItemDecoration(divider)
        binding.skCountPortion.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ingredientsAdapter.updateIngredients(progress)
                ingredientsAdapter.notifyDataSetChanged()
                binding.tvPortionCount.text = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun initUI() {
        val inputStream: InputStream? =
            recipe?.imageUrl?.let { binding.ivRecipe.context?.assets?.open(it) }
        val drawable = Drawable.createFromStream(inputStream, null)
        binding.ivRecipe.setImageDrawable(drawable)

        favoritesSet = getFavorites()
        val recipeId = recipe?.id.toString()
        var isRecipeInSet = recipeId in favoritesSet.orEmpty()

        if (isRecipeInSet) {
            binding.ibHeart.setImageResource(R.drawable.ic_heart)
        } else {
            binding.ibHeart.setImageResource(R.drawable.ic_heart_empty)
        }

        binding.ibHeart.setOnClickListener {
            if (isRecipeInSet) {
                binding.ibHeart.setImageResource(R.drawable.ic_heart_empty)
                favoritesSet?.remove(recipeId)
            } else {
                binding.ibHeart.setImageResource(R.drawable.ic_heart)
                favoritesSet?.add(recipeId)
            }
            isRecipeInSet = !isRecipeInSet
            saveFavorites(favoritesSet?.toSet() ?: emptySet())
            favoritesSet = getFavorites()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun saveFavorites(favoritesSet: Set<String>) {
        val sharedPrefs =
            requireContext().getSharedPreferences(FAVORITES_PREFS_NAME, Context.MODE_PRIVATE)
        sharedPrefs?.edit {
            putStringSet(KEY_FAVORITES_SET, favoritesSet)
        }
    }

    fun getFavorites(): HashSet<String> {
        val sharedPrefs =
            requireContext().getSharedPreferences(FAVORITES_PREFS_NAME, Context.MODE_PRIVATE)
        return HashSet(sharedPrefs?.getStringSet(KEY_FAVORITES_SET, emptySet()) ?: emptySet())
    }
}