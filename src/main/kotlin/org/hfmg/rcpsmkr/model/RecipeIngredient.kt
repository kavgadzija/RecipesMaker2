package org.hfmg.rcpsmkr.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipeIngredient(
    var ingredient: Ingredient,
    var cantidad: String
)
