package ru.marinarodionova.recipesapp

const val FAVORITES_PREFS_NAME = "favorites"
const val KEY_FAVORITES_SET = "favorites_set"
const val GET_IMG_API = "https://recipes.androidsprint.ru/api/images/"

enum class LoadingStatus {
    NOT_READY,
    READY,
    FAILED
}