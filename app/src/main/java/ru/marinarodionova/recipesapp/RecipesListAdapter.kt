package ru.marinarodionova.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.marinarodionova.recipesapp.models.Recipe
import java.io.InputStream
import ru.marinarodionova.recipesapp.databinding.ItemRecipeBinding

class RecipesListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRecipeBinding.bind(view)
        val imageView: ImageView = binding.imageRecipe
        val titleTextView: TextView = binding.titleRecipe
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.item_recipe, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val recipe: Recipe = dataSet[position]
        with(viewHolder) {
            titleTextView.text = recipe.title
            val inputStream: InputStream? =
                try {
                    itemView.context?.assets?.open(dataSet[position].imageUrl)
                } catch (e: Exception) {
                    Log.d("!!!", "Image not found ${recipe.imageUrl}")
                    null
                }
            val drawable = Drawable.createFromStream(inputStream, null)
            imageView.setImageDrawable(drawable)
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(recipeId = position)
            }
        }

    }

    override fun getItemCount() = dataSet.size
}