package org.hfmg.rcpsmkr.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipeBook(
    var name: String,
    var categories: MutableList<Category>,
    var ingredients: MutableList<Ingredient>,
    var recipes: MutableList<Recipe>
)
