package ru.marinarodionova.recipesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.marinarodionova.recipesapp.databinding.ItemIngredientBinding
import ru.marinarodionova.recipesapp.models.Ingredient

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
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
        val quantityFloat = ingredient.quantity.toFloatOrNull() ?: 0f
        val result = quantityFloat * quantity
        val countFormatted = if (result % 1f == 0f) {
            result.toInt().toString()
        } else {
            String.format("%.1f", result)
        }
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
}