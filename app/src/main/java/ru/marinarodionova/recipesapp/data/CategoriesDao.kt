package ru.marinarodionova.recipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.marinarodionova.recipesapp.models.Category

@Dao
interface CategoriesDao{
    @Query("SELECT * FROM category")
    suspend fun getCategories(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(vararg category:Category)
}