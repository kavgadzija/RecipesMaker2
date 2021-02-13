package org.hfmg.rcpsmkr.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipeBook(
    var name: String,
    var groups: MutableList<Group>,
    var ingredients: MutableList<Ingredient>,
    var categories: MutableList<Category>,
    var recipes: MutableList<Recipe>
)
