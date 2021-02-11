package org.hfmg.rcpsmkr

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hfmg.rcpsmkr.model.*
import java.io.File

class RecipesMaker() {
    private lateinit var recipeBook: RecipeBook
    private var ultIDRec: Int = 0
    private lateinit var datasource: String

    init {
        recipeBook = RecipeBook(
            name = "",
            categories =mutableListOf<Category>(),
            ingredients = mutableListOf<Ingredient>(),
            recipes = mutableListOf<Recipe>()
        )
    }

    private fun createData() {
        recipeBook.name = "Recetario Personal"
        recipeBook.ingredients.add(Ingredient("Agua"))
        recipeBook.ingredients.add(Ingredient("Leche"))
        recipeBook.ingredients.add(Ingredient("Carne"))
        recipeBook.ingredients.add(Ingredient("Verduras"))
        recipeBook.ingredients.add(Ingredient("Frutas"))
        recipeBook.ingredients.add(Ingredient("Cereal"))
        recipeBook.ingredients.add(Ingredient("Huevos"))
        recipeBook.ingredients.add(Ingredient("Aceite"))
        recipeBook.ingredients.add(Ingredient("Choclo"))
        recipeBook.ingredients.add(Ingredient("Queso"))
        recipeBook.ingredients.add(Ingredient("Sal"))

        val ctg1 = Category("Veganas")
        val ctg2 = Category("Vegetarianas")
        val ctg3 = Category("Peruana")
        val ctg4 = Category("Peruana")
        val ctg5 = Category("Andina")
        val ctg6 = Category("Marina")
        val ctg7 = Category("Peruana")
        val ctg8 = Category("Francesa")
        val ctg9 = Category("Pastas")
        val ctg10 = Category("Postres")

        recipeBook.categories.add(ctg1)
        recipeBook.categories.add(ctg2)
        recipeBook.categories.add(ctg3)
        recipeBook.categories.add(ctg4)
        recipeBook.categories.add(ctg5)
        recipeBook.categories.add(ctg6)
        recipeBook.categories.add(ctg7)
        recipeBook.categories.add(ctg8)
        recipeBook.categories.add(ctg9)
        recipeBook.categories.add(ctg10)

        val recipe1 = Recipe("Choclo con queso",
            categories = mutableSetOf<Category>(),
            ingredients =mutableListOf<RecipeIngredient>(),
            instructions = mutableListOf<String>()
        )
        recipe1.categories.add(ctg2)
        recipe1.categories.add(ctg3)

        val recipeIngredient1 = RecipeIngredient(recipeBook.ingredients[8], "8 unidades")
        val recipeIngredient2 = RecipeIngredient(recipeBook.ingredients[9], "1 kg")
        val recipeIngredient3 = RecipeIngredient(recipeBook.ingredients[10], "Al gusto")
        recipe1.ingredients.add(recipeIngredient1)
        recipe1.ingredients.add(recipeIngredient2)
        recipe1.ingredients.add(recipeIngredient3)

        recipe1.instructions.add("Pelar y limpiar las mazorcas de maiz")
        recipe1.instructions.add("Sancochar los choclos durante 20 minutos en agua con sal")
        recipe1.instructions.add("Sevir 2 choclos mas 4 rebanadas de queso fresco por porcion")

        recipeBook.recipes.add(recipe1)
    }

    private  fun readFile(jsonFile: String): String {
        val file = File(jsonFile)
        val stringJson:String = file.readText()
        println(stringJson)
        return stringJson
    }

    private  fun writeFile(jsonFile: String, jsonString: String) {
        val fileObject = File(jsonFile)
        // create a new file
        fileObject.writeText(jsonString)
    }

    private fun loadData(jsonFile: String): Boolean {
        /*
        recipeBook = RecipeBook(
            name ="",
            ingredients =mutableListOf<Ingredient>(),
            recipes = mutableListOf<Recipe>()
        )
        createData()
         */
        val stringJson: String = readFile(jsonFile)
        if (stringJson != "") {
            recipeBook = Json.decodeFromString<RecipeBook>(stringJson)
            return true
        } else {
            return false
        }
    }

    private fun saveData(jsonFile: String): Boolean {
        // Serializing objects
        val stringJson = Json.encodeToString(recipeBook)
        writeFile(jsonFile, stringJson)
        /*
        println("----------------------JSON-------------------------------")
        println(stringJson)
        // Deserializing back into objects
        println("---------------------OBJECT------------------------------")
        recipeBook = Json.decodeFromString<RecipeBook>(stringJson)
        println(recipeBook)
         */
        return true
    }

    private fun showMessage(mensaje: String) {
        print(mensaje)
        val toString = readLine().toString()
    }

    private fun listCategories() {
        for (category in recipeBook.categories) {
            val index = recipeBook.categories.indexOf(category)
            println("[${index+1}] - ${category.name}")
        }
    }

    private fun listIngredients() {
        for (ingredient in recipeBook.ingredients) {
            val index = recipeBook.ingredients.indexOf(ingredient)
            println("[${index+1}] - ${ingredient.name}")
        }
    }

    private fun listRecipes() {
        for (recipe in recipeBook.recipes) {
            val index = recipeBook.recipes.indexOf(recipe)
            println("[${index+1}] - ${recipe.title}")
        }
    }

    private fun listCategoriesRecipe(recipe: Recipe) {
        if (recipe.categories.isNotEmpty()) {
            for (category in recipe.categories) {
                val index = recipe.categories.indexOf(recipe)
                println("${index+1}. ${category.name}")
            }
        } else {
            println("<--- Sin definir --->")
        }
    }

    private fun listIngredientsRecipe(recipe: Recipe) {
        if (recipe.categories.isNotEmpty()) {
            for (ingredientRecipe in recipe.ingredients) {
                val index = recipe.ingredients.indexOf(ingredientRecipe)
                println("${index+1}. ${ingredientRecipe.ingredient.name} (${ingredientRecipe.cantidad})")
            }
        } else {
            println("<--- Sin definir --->")
        }
    }

    private fun listInstuctionsRecipe(recipe: Recipe) {
        if (recipe.categories.isNotEmpty()) {
            for (instruction in recipe.instructions) {
                val index = recipe.instructions.indexOf(instruction)
                println("${index+1}. ${instruction}. ")
            }
        } else {
            println("<--- Sin definir --->")
        }
    }

    private fun createCategory() {
        println("------------------------------------------------------")
        print("Ingresar el nombre de la nueva categoria: ")
        val name = readLine().toString()
        if (name != "") {
            recipeBook.categories.add(Category(name))
            showMessage("Se creo la nueva categoria ... ")
        } else {
            showMessage("No ingreso ningun nombre ... ")
        }
    }

    private fun replaceCategory() {
        showMessage(" Rempazar Categoria ... ")
    }

    private fun removeCategory() {
        showMessage(" Eliminar Categoria ... ")
    }

    private fun showCategories() {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("CATEGORIAS:")
            listCategories()
            println("------------------------------------------------------")
            print("Usted desea [C]rear, [R]eemplazar, [B]orrar o [S]alir: ")
            val opcion = readLine().toString()

            when (opcion) {
                "C", "c" -> createCategory()
                "R", "r" -> replaceCategory()
                "B", "b" -> removeCategory()
                "S", "s" -> salir = true
                else -> showMessage("Las opciones válidas son C, R, B, o S ... ")
            }
        }
    }

    private fun createIngredient() {
        println("------------------------------------------------------")
        print("Ingresar el nombre del nuevo ingrediente: ")
        val name = readLine().toString()
        if (name != "") {
            recipeBook.ingredients.add(Ingredient(name))
            showMessage("Se creo el nuevo ingrediente ... ")
        } else {
            showMessage("No ingreso ningun nombre ... ")
        }
    }

    private fun replaceIngredient() {
        showMessage(" Rempazar Ingrediente ... ")
    }

    private fun removeIngredient() {
        showMessage(" Eliminar Ingrediente ... ")
    }

    private fun showIngredients() {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("INGREDIENTES:")
            listIngredients()
            println("------------------------------------------------------")
            print("Usted desea [C]rear, [R]eemplazar, [B]orrar o [S]alir: ")
            val opcion = readLine().toString()

            when (opcion) {
                "C", "c" -> createIngredient()
                "R", "r" -> replaceIngredient()
                "B", "b" -> removeIngredient()
                "S", "s" -> salir = true
                else -> showMessage("Las opciones válidas son C, R, B, o S ... ")
            }
        }
    }

    private fun searchRecipe(titulo: String): Boolean {
        var encontrada = false
        for (recipe in recipeBook.recipes) {
            encontrada = recipe.title == titulo
            if (encontrada) break
        }
        return encontrada
    }

    private fun inputName(recipe: Recipe): Recipe {
        print("Ingrese el nombre de la receta: ")
        val title = readLine().toString()
        if (title.isEmpty()) {
            println("Ingreso un titulo vacio")
            return recipe
        } else {
            if (searchRecipe(title)) {
                println("Ingreso un titulo ya registrado")
                return recipe
            } else {
                println("El titulo fue registrado")
                recipe.title = title
                return recipe
            }
        }
    }

    private fun inputCategories(recipe: Recipe) {
        showMessage("Ingresando Categorias ... ")
    }

    private fun inputIngredients(recipe: Recipe) {
        showMessage("Ingresando Ingredientes ... ")
    }

    private fun inputInstructions(recipe: Recipe) {
        showMessage("Ingresando Instucciones ... ")
    }

    private fun showRecipe(recipe: Recipe) {
        println("..................................................................................")
        println("RECETA:")
        println('"' + recipe.title + '"')
        println("CATEGORIAS:")
        listCategoriesRecipe(recipe)
        println("INGREDIENTES:")
        listIngredientsRecipe(recipe)
        println("INSTRUCCIONES:")
        listInstuctionsRecipe(recipe)
        println("..................................................................................")
        showMessage("Mostrando Receta ... ")
    }

    private fun saveRecipe(recipe: Recipe): Boolean {
        showMessage("Guardando Receta ... ")
        return true
    }

    private fun createRecipe() {
        val recipe = Recipe("",
            categories = mutableSetOf<Category>(),
            ingredients =mutableListOf<RecipeIngredient>(),
            instructions = mutableListOf<String>()
        )
        var salir = false
        while (!salir) {
            println(
                """
                    ------------------------------------------------------
                    RECETA:
                    [1] - Titulo
                    [2] - Categorias
                    [3] - Ingredientes
                    [4] - Instucciones
                    [5] - Mostrar
                    [S] - Salir
                    ------------------------------------------------------
                """.trimIndent()
            )
            print("Elegir opcion: ")
            val opcion = readLine().toString()
            when (opcion) {
                "1" -> inputName(recipe)
                "2" -> inputCategories(recipe)
                "3" -> inputIngredients(recipe)
                "4" -> inputInstructions(recipe)
                "5" -> showRecipe(recipe)
                "S", "s" -> salir = saveRecipe(recipe)
                else -> showMessage("Las opciones válidas son 1, 2, 3, 4, 5 o S ... ")
            }
        }
    }

    private fun replaceRecipe() {
        showMessage(" Rempazar Receta ... ")
    }

    private fun removeRecipe() {
        showMessage(" Eliminar Receta ... ")
    }

    private fun showRecipes() {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("RECETAS:")
            listRecipes()
            println("------------------------------------------------------")
            print("Usted desea [C]rear, [E]ditar, [B]orrar o [S]alir: ")
            val opcion = readLine().toString()

            when (opcion) {
                "C", "c" -> createRecipe()
                "E", "e" -> replaceRecipe()
                "B", "b" -> removeRecipe()
                "S", "s" -> salir = true
                else -> showMessage("Las opciones válidas son C, E, B, o S ... ")
            }
        }
    }

    private fun showMainMenu() {
        var opcion = ""
        while (opcion != "S" && opcion != "s") {
            println(
                """
                    ------------------------------------------------------
                    MENU PRINCIPAL
                    [1] - Categorias
                    [2] - Ingredientes
                    [3] - Recetas
                    [S] - Salir
                    ------------------------------------------------------
                """.trimIndent()
            )
            print("Elegir opcion: ")
            opcion = readLine().toString()
            when (opcion) {
                "1" -> showCategories()
                "2" -> showIngredients()
                "3" -> showRecipes()
                "S", "s" -> showMessage("*** Hasta Pronto *** ... ")
                else -> {
                    showMessage("Las opciones válidas son 1, 2, 3 o S ... ")
                }
            }
        }
    }

    fun run(jsonFile: String) {
        /*
        if (loadData(jsonFile)) {
            println("---------------------OBJECT------------------------------")
            println(recipeBook)
        }
         */
        loadData(jsonFile)
        showMainMenu()

        //createData()
        //saveData(jsonFile)
    }

}

fun main(args : Array<String>) {
    val rm = RecipesMaker()
    rm.run("recipe-book.json")
}