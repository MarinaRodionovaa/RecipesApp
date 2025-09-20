package ru.marinarodionova.recipesapp.data

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import ru.marinarodionova.recipesapp.models.Ingredient

object Converters {
    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }

    @TypeConverter
    @JvmStatic
    fun ingredientListToString(v: List<Ingredient>): String =
        json.encodeToString(ListSerializer(Ingredient.serializer()), v)

    @TypeConverter
    @JvmStatic
    fun stringToIngredientList(v: String): List<Ingredient> =
        json.decodeFromString(ListSerializer(Ingredient.serializer()), v)

    @TypeConverter
    @JvmStatic
    fun stringListToString(v: List<String>): String =
        json.encodeToString(ListSerializer(String.serializer()), v)

    @TypeConverter
    @JvmStatic
    fun stringToStringList(v: String): List<String> =
        json.decodeFromString(ListSerializer(String.serializer()), v)
}