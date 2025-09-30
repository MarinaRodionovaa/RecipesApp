package ru.marinarodionova.recipesapp.ui.recipeUi.recipe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.marinarodionova.recipesapp.databinding.FragmentRecipeBinding
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import ru.marinarodionova.recipesapp.LoadingStatus
import ru.marinarodionova.recipesapp.R
import ru.marinarodionova.recipesapp.RecipesApplication

class PortionSeekBarListener(
    private val onChangeIngredients: (Int) -> Unit
) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        onChangeIngredients(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}

class RecipeFragment : Fragment() {
    private var recipeId: Int? = null
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not be null")
    private lateinit var viewModel: RecipeViewModel
    val args: RecipeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        viewModel = appContainer.recipeViewModelFactory.create()
    }

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
        recipeId = args.recipeId
        viewModel.loadRecipe(recipeId ?: return)
        initUI()
    }

    private fun initUI() {
        val ingredientsAdapter = IngredientsAdapter(emptyList())
        val methodAdapter = MethodAdapter(emptyList())
        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.loadingStatus == LoadingStatus.FAILED) {
                Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            }
            ingredientsAdapter.setIngredients(state.ingredientList.filterNotNull())
            ingredientsAdapter.updateIngredients(state.portionCount)
            binding.rvIngredients.adapter = ingredientsAdapter

            methodAdapter.setMethod(state.method.filterNotNull())
            binding.rvMethod.adapter = methodAdapter

            binding.tvRecipeTitle.text = state.recipeName
            Glide.with(this).load(state.recipeImg)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivRecipe)
            val isFavorite = state.isFavorite ?: false
            binding.ibHeart.setImageResource(
                if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
            )
            binding.tvPortionCount.text = state.portionCount.toString()
            binding.skCountPortion.progress = state.portionCount

            binding.ibHeart.setOnClickListener {
                state.recipeId?.let { viewModel.onFavoritesClicked(it) }
            }
        }

        binding.skCountPortion.setOnSeekBarChangeListener(
            PortionSeekBarListener { progress ->
                viewModel.updatePortionCount(progress)
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