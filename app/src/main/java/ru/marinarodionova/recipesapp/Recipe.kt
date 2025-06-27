package ru.marinarodionova.recipesapp

import java.net.URL

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: String,
    val imageUrl: URL,

    )