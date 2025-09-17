package ru.marinarodionova.recipesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.marinarodionova.recipesapp.models.Category
import ru.marinarodionova.recipesapp.models.RecipeEntity

@Database(entities = [Category::class, RecipeEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoriesDao
    abstract fun recipesDao(): RecipesDao
}