package ru.marinarodionova.recipesapp.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String,
) : Parcelable

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "ingredients") val ingredients: List<Ingredient>,
    @ColumnInfo(name = "method") val method: List<String>,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
    @ColumnInfo(name = "categoryId") val categoryId: Int,
    @ColumnInfo(name = "isFavorite") val isFavorite: Boolean,
)

fun Recipe.toEntity(categoryId: Int, isFavorite: Boolean): RecipeEntity =
    RecipeEntity(
        id = id,
        title = title,
        ingredients = ingredients,
        method = method,
        imageUrl = imageUrl,
        categoryId = categoryId,
        isFavorite = isFavorite
    )

fun RecipeEntity.toDomain(): Recipe =
    Recipe(
        id = id,
        title = title,
        ingredients = ingredients,
        method = method,
        imageUrl = imageUrl
    )