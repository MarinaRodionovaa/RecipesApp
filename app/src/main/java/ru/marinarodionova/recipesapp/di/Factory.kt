package ru.marinarodionova.recipesapp.di

interface Factory<T> {
    fun create(): T
}