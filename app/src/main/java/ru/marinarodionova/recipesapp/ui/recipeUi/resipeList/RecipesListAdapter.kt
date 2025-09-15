package ru.marinarodionova.recipesapp.ui.recipeUi.resipeList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.marinarodionova.recipesapp.GET_IMG_API
import ru.marinarodionova.recipesapp.R
import ru.marinarodionova.recipesapp.models.Recipe
import ru.marinarodionova.recipesapp.databinding.ItemRecipeBinding

class RecipesListAdapter(var dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

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
            Glide.with(itemView.context)
                .load("$GET_IMG_API${dataSet[position].imageUrl}")
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(imageView)
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(recipeId = recipe.id)
            }
        }

    }

    override fun getItemCount() = dataSet.size

    fun setRecipeList(recipeList: List<Recipe>) {
        dataSet = recipeList
        notifyDataSetChanged()
    }
}