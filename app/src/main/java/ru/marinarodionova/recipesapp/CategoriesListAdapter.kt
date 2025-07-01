package ru.marinarodionova.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.marinarodionova.recipesapp.models.Category
import java.io.InputStream

class CategoriesListAdapter(private val dataSet: List<Category>) :

    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_category)
        val titleTextView: TextView = view.findViewById(R.id.title_category)
        val descriptionTextView: TextView = view.findViewById(R.id.description_category)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.item_category, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category: Category = dataSet[position]
        viewHolder.titleTextView.text = category.title
        viewHolder.descriptionTextView.text = category.description

        val inputStream: InputStream? =
            try {
                viewHolder.itemView.context?.assets?.open(dataSet[position].imageUrl)
            } catch (e: Exception) {
                Log.d("!!!", "Image not found ${category.imageUrl}")
                null
            }
        val drawable = Drawable.createFromStream(inputStream, null)
        viewHolder.imageView.setImageDrawable(drawable)
    }

    override fun getItemCount() = dataSet.size
}