package ru.marinarodionova.recipesapp.ui.recipeUi.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.marinarodionova.recipesapp.R
import ru.marinarodionova.recipesapp.databinding.ItemIngredientBinding
import ru.marinarodionova.recipesapp.models.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(var dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    private var quantity = 1

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemIngredientBinding.bind(view)
        val titleTextView: TextView = binding.nameIngredient
        val countTextView: TextView = binding.countIngredient
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.item_ingredient, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]
        val countFormatted = BigDecimal(ingredient.quantity)
            .multiply(BigDecimal(quantity))
            .setScale(1, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
        val countMeasure = "$countFormatted ${ingredient.unitOfMeasure}"

        with(viewHolder) {
            titleTextView.text = ingredient.description
            countTextView.text = countMeasure
        }
    }

    override fun getItemCount() = dataSet.size

    fun updateIngredients(progress: Int) {
        quantity = progress
    }

    fun setIngredients(ingredients: List<Ingredient>){
        dataSet = ingredients
    }
}