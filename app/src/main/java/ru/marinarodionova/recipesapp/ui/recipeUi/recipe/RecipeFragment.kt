package ru.marinarodionova.recipesapp.ui.recipeUi.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.marinarodionova.recipesapp.databinding.FragmentRecipeBinding
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.fragment.app.viewModels
import ru.marinarodionova.recipesapp.ARG_RECIPE
import ru.marinarodionova.recipesapp.R

class RecipeFragment : Fragment() {
    private var recipeId: Int? = null
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")
    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) { state ->
            Log.d("!!!!", state.isFavorite.toString())
        }
        recipeId = arguments?.getInt(ARG_RECIPE)
        viewModel.loadRecipe(recipeId ?: return)
        initUI()
    }

    private fun initUI() {
        viewModel.state.observe(viewLifecycleOwner) { state ->

            val ingredientsAdapter = IngredientsAdapter(state.ingredientList.filterNotNull())
            ingredientsAdapter.updateIngredients(state.portionCount)
            binding.rvIngredients.adapter = ingredientsAdapter

            val methodAdapter = MethodAdapter(state.method.filterNotNull())
            binding.rvMethod.adapter = methodAdapter

            binding.tvRecipeTitle.text = state.recipeName
            binding.ivRecipe.setImageDrawable(state.recipeImg)
            val isFavorite = state.isFavorite ?: false
            binding.ibHeart.setImageResource(
                if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
            )
            binding.tvPortionCount.text = state.portionCount.toString()
            binding.skCountPortion.progress = state.portionCount

            binding.ibHeart.setOnClickListener {
                viewModel.onFavoritesClicked(binding)
            }
        }

        binding.skCountPortion.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                viewModel.updatePortionCount(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}