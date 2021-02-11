package org.hfmg.rcpsmkr.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    var title: String,
    var categories: MutableSet<Category>,
    var ingredients: MutableList<RecipeIngredient>,
    var instructions: MutableList<String>
)
