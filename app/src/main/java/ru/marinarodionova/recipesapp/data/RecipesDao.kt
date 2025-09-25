package ru.marinarodionova.recipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.marinarodionova.recipesapp.models.RecipeEntity

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipes WHERE categoryId == :categoryId")
    suspend fun getRecipesByCategoryId(categoryId: Int): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE id == :recipeId")
    suspend fun getRecipeById(recipeId: Int): RecipeEntity?

    @Query("SELECT * FROM recipes WHERE id IN (:ids)")
    suspend fun getRecipesById(ids: Set<Int>): List<RecipeEntity>

    @Query("SELECT id FROM recipes WHERE isFavorite == 1")
    suspend fun getFavoritesIds(): List<Int>

    @Query("SELECT * FROM recipes WHERE isFavorite == 1")
    suspend fun getFavoritesRecipes(): List<RecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(vararg recipe: RecipeEntity)
}