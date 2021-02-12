package org.hfmg.rcpsmkr

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hfmg.rcpsmkr.model.*
import java.io.File

class RecipesMaker : CliktCommand() {
    private val datasource: String by option(help="Fuente de Datos (archivo JSON)").prompt("Su fuente de datos JSON")
    private lateinit var recipeBook: RecipeBook

    init {
        recipeBook = RecipeBook(
            name = "",
            categories =mutableListOf<Category>(),
            ingredients = mutableListOf<Ingredient>(),
            recipes = mutableListOf<Recipe>()
        )
    }

    private  fun readFile(jsonFile: String): String {
        var stringJson: String = ""
        val file = File(jsonFile)
        if (file.exists()) {
            stringJson = file.readText()
        }
        return stringJson
    }

    private  fun writeFile(jsonFile: String, jsonString: String) {
        val fileObject = File(jsonFile)
        fileObject.writeText(jsonString)
    }

    private fun loadData(jsonFile: String): Boolean {
        val stringJson: String = readFile(jsonFile)
        if (stringJson != "") {
            recipeBook = Json.decodeFromString<RecipeBook>(stringJson)
            return true
        } else {
            return false
        }
    }

    private fun saveData(jsonFile: String): Boolean {
        val stringJson = Json.encodeToString(recipeBook)
        writeFile(jsonFile, stringJson)
        return true
    }

    private fun showMessage(mensaje: String) {
        print(mensaje)
        val toString = readLine().toString()
    }

    private fun listCategories() {
        if (recipeBook.categories.isNotEmpty()) {
            for (category in recipeBook.categories) {
                val index = recipeBook.categories.indexOf(category)
                println("[${index+1}] - ${category.name}")
            }
        } else {
            println("<--- Sin Categorías --->")
        }
    }

    private fun listIngredients() {
        if (recipeBook.ingredients.isNotEmpty()) {
            for (ingredient in recipeBook.ingredients) {
                val index = recipeBook.ingredients.indexOf(ingredient)
                println("[${index+1}] - ${ingredient.name}")
            }
        } else {
            println("<--- Sin Ingredientes --->")
        }
    }

    private fun listRecipes() {
        if (recipeBook.recipes.isNotEmpty()) {
            for (recipe in recipeBook.recipes) {
                val index = recipeBook.recipes.indexOf(recipe)
                println("[${index+1}] - ${recipe.title}")
            }
        } else {
            println("<--- Sin Recetas --->")
        }
    }

    private fun listRecipeCategories(recipe: Recipe) {
        if (recipe.categories.isNotEmpty()) {
            var categories = ""
            for (category in recipe.categories) {
                val index = recipe.categories.indexOf(category)
                //println("${index+1}. ${category.name}")
                categories += category.name
                if (index+1 < recipe.categories.size) {
                    categories += ", "
                }
            }
            println("($categories)")
        } else {
            println("<--- Sin definir --->")
        }
    }

    private fun listRecipeIngredients(recipe: Recipe) {
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
        print("Ingresar el nombre de la nueva categoría: ")
        val name = readLine().toString()
        if (name != "") {
            recipeBook.categories.add(Category(name))
            recipeBook.categories.sortBy { it.name }
            showMessage("Se creo la nueva categoría ... ")
        } else {
            showMessage("No ingreso ningun nombre ... ")
        }
    }

    private fun modifyCategory() {
        println("------------------------------------------------------")
        print("Ingrese el código de la categoría a modificar: ")
        val code = readLine().toString()
        if (code.isNotEmpty()) {
            try {
                val index: Int =  code.toInt() - 1
                val category = recipeBook.categories[index]
                print("Cambiar [${category.name}] por el nuevo nombre: ")
                val name = readLine().toString()
                if (name != "") {
                    category.name = name
                    //recipeBook.categories[index] = category
                    recipeBook.categories.set(index, category)
                    recipeBook.categories.sortBy { it.name }
                    showMessage("Se ha cambiado el nombre de la categoría ... ")
                } else {
                    showMessage("No ingreso ningun nombre ... ")
                }
            } catch (exception: Exception) {
                showMessage("Debe ingresar un código de categoría válido ... ")
            }
        }
    }

    private fun removeCategory() {
        println("------------------------------------------------------")
        print("Ingrese el código de la categoría a remover: ")
        val code = readLine().toString()
        if (code.isNotEmpty()) {
            try {
                val index: Int =  code.toInt() - 1
                val category = recipeBook.categories[index]
                print("Confirma remover [${category.name}] (Si={S/s}  o  No={N/n}): ")
                val opcion = readLine().toString()
                when (opcion) {
                    "N", "n" -> showMessage("Operación cancelada ... ")
                    "S", "s" -> {
                        recipeBook.categories.remove(category)
                        //recipeBook.ingredients.removeAt(index)
                        showMessage("Se ha removido el ingrediente ... ")
                    }
                    else -> showMessage("Las opciones válidas son {S/s} o {N/n} ... ")
                }
            } catch (exception: Exception) {
                showMessage("Debe ingresar un código de ingrediente válido ... ")
            }
        }
    }

    private fun showCategories() {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("CATEGORIAS:")
            listCategories()
            println("------------------------------------------------------")
            print("Usted desea [C]rear, [M]odificar, [R]emover o [S]alir: ")
            val opcion = readLine().toString()

            when (opcion) {
                "C", "c" -> createCategory()
                "M", "m" -> modifyCategory()
                "R", "r" -> removeCategory()
                "S", "s" -> salir = true
                else -> showMessage("Las opciones válidas son C, M, R, o S ... ")
            }
        }
    }

    private fun createIngredient() {
        println("------------------------------------------------------")
        print("Ingresar el nombre del nuevo ingrediente: ")
        val name = readLine().toString()
        if (name != "") {
            recipeBook.ingredients.add(Ingredient(name, Group("Generico")))
            recipeBook.ingredients.sortBy { it.name }
            showMessage("Se ha creado el nuevo ingrediente ... ")
        } else {
            showMessage("No ingreso ningun nombre ... ")
        }
    }

    private fun modifyIngredient() {
        println("------------------------------------------------------")
        print("Ingrese el código del ingrediente a modificar: ")
        val code = readLine().toString()
        if (code.isNotEmpty()) {
            try {
                val index: Int =  code.toInt() - 1
                val ingredient = recipeBook.ingredients[index]
                print("Cambiar [${ingredient.name}] por el nuevo nombre: ")
                val name = readLine().toString()
                if (name != "") {
                    ingredient.name = name
                    recipeBook.ingredients.set(index, ingredient)
                    recipeBook.ingredients.sortBy { it.name }
                    showMessage("Se ha cambiado el nombre del ingrediente ... ")
                } else {
                    showMessage("No ingreso ningun nombre ... ")
                }
            } catch (exception: Exception) {
                showMessage("Debe ingresar un código de ingrediente válido ... ")
            }
        }
    }

    private fun removeIngredient() {
        println("------------------------------------------------------")
        print("Ingrese el código del ingrediente a remover: ")
        val code = readLine().toString()
        if (code.isNotEmpty()) {
            try {
                val index: Int =  code.toInt() - 1
                val ingredient = recipeBook.ingredients[index]
                print("Confirma remover [${ingredient.name}] (Si={S/s}  o  No={N/n}): ")
                val opcion = readLine().toString()
                when (opcion) {
                    "N", "n" -> showMessage("Operación cancelada ... ")
                    "S", "s" -> {
                        recipeBook.ingredients.remove(ingredient)
                        //recipeBook.ingredients.removeAt(index)
                        showMessage("Se ha removido el ingrediente ... ")
                    }
                    else -> showMessage("Las opciones válidas son {S/s} o {N/n} ... ")
                }
            } catch (exception: Exception) {
                showMessage("Debe ingresar un código de ingrediente válido ... ")
            }
        }
    }

    private fun showIngredients() {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("INGREDIENTES:")
            listIngredients()
            println("------------------------------------------------------")
            print("Usted desea [C]rear, [M]odificar, [R]emover o [S]alir: ")
            val opcion = readLine().toString()

            when (opcion) {
                "C", "c" -> createIngredient()
                "M", "m" -> modifyIngredient()
                "R", "r" -> removeIngredient()
                "S", "s" -> salir = true
                else -> showMessage("Las opciones válidas son C, M, R, o S ... ")
            }
        }
    }

    private fun searchRecipe(título: String): Boolean {
        var encontrada = false
        for (recipe in recipeBook.recipes) {
            encontrada = recipe.title == título
            if (encontrada) break
        }
        return encontrada
    }

    private fun inputName(recipe: Recipe): Recipe {
        print("Ingrese el título de la receta: ")
        val title = readLine().toString()
        if (title.isEmpty()) {
            println("Ha ingresado un título vacio")
            return recipe
        } else {
            if (searchRecipe(title)) {
                println("Ha ingresado un título que ya existe")
                return recipe
            } else {
                println("El título fue registrado")
                recipe.title = title
                return recipe
            }
        }
    }

    private fun inputCategories(recipe: Recipe): Recipe {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("CATEGORIAS DE LA RECETA:")
            listRecipeCategories(recipe)
            println("------------------------------------------------------")
            println("CATEGORIAS DISPONIBLES:")
            listCategories()
            println("------------------------------------------------------")
            print("Elegir un código de categoría [?] o salir con [S]: ")
            val opcion = readLine().toString()
            if (opcion == "S" || opcion == "s") {
                salir = true
            } else {
                if (opcion != "") {
                    try {
                        val category = recipeBook.categories[opcion.toInt()-1]
                        recipe.categories.add(category)
                        recipe.categories.sortedBy { it.name }
                    } catch (exception: Exception) {
                        showMessage("Debe ingresar un código de categoría válido ... ")
                    }
                } else {
                    showMessage("Debe ingresar un código de categoría válido ... ")
                }
            }
        }
        return recipe
    }

    private fun inputIngredients(recipe: Recipe): Recipe {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("INGREDIENTES DE LA RECETA:")
            listRecipeIngredients(recipe)
            println("------------------------------------------------------")
            println("INGREDIENTES DISPONIBLES:")
            listIngredients()
            println("------------------------------------------------------")
            print("Elegir un código de ingrediente [?] o salir con [S]: ")
            val opcion = readLine().toString()
            if (opcion == "S" || opcion == "s") {
                salir = true
            } else {
                if (opcion != "") {
                    try {
                        val ingredient = recipeBook.ingredients[opcion.toInt()-1]
                        var cantidad = ""
                        do {
                            print("Indicar la cantidad de [${ingredient.name}]: ")
                            cantidad = readLine().toString()
                            if (cantidad.isNotEmpty()) {
                                val recipeIngredient = RecipeIngredient(ingredient, cantidad)
                                recipe.ingredients.add(recipeIngredient)
                                recipe.ingredients.sortBy { it.ingredient.name }
                            } else {
                                showMessage("Debe indicar una cantidad de [${ingredient.name}] válida ... ")
                            }
                        } while (cantidad == "")
                    } catch (exception: Exception) {
                        showMessage("Debe ingresar un código de ingrediente válido ... ")
                    }
                } else {
                    showMessage("Debe ingresar un código de ingrediente válido ... ")
                }
            }
        }
        return recipe
    }

    private fun inputInstructions(recipe: Recipe): Recipe {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("PASOS PARA PREPARAR LA RECETA:")
            listInstuctionsRecipe(recipe)
            println("------------------------------------------------------")
            println("Ingresar una acción para elaborar la receta (o Cero terminar): ")
            val paso = readLine().toString()
            if (paso.isNotEmpty()) {
                if (paso != "0") {
                    recipe.instructions.add(paso)
                } else {
                    salir = true
                }
            } else {
                showMessage("Debe ingresar un paso no vacio ... ")
            }
        }
        return recipe
    }

    private fun showRecipe(recipe: Recipe) {
        println("..................................................................................")
        println("RECETA:")
        println(recipe.title)
        //println('"' + recipe.title + '"')
        //println("CATEGORIAS:")
        listRecipeCategories(recipe)
        println("INGREDIENTES:")
        listRecipeIngredients(recipe)
        println("INSTRUCCIONES:")
        listInstuctionsRecipe(recipe)
        println("..................................................................................")
        showMessage("Mostrando Receta ... ")
    }

    private fun saveRecipe(recipe: Recipe): Boolean {
        var salir = false
        var guardada = false
        while (!salir) {
            print("Usted guarda la receta (Si={S/s}  o  No={N/n}: ")
            val opcion = readLine().toString()
            when (opcion) {
                "N","n" -> salir = true
                "S","s" -> {
                    if (validateRecipe(recipe)) {
                        recipeBook.recipes.add(recipe)
                        recipeBook.recipes.sortBy { it.title }
                        guardada = true
                    } else {
                        showMessage("Los datos de la receta están incompletos ... ")
                    }
                    salir = true
                }
                else -> {
                    showMessage("Las opciones válidas son {S/s}, {N/n} o Cero ... ")
                }
            }
        }
        return guardada
    }

    private fun validateRecipe(recipe: Recipe): Boolean {
        val titleOk = !recipe.title.isEmpty()
        val ingredientsOk = recipe.ingredients.isNotEmpty()
        val instructionsOk = !recipe.instructions.isNullOrEmpty()
        return (titleOk && ingredientsOk && instructionsOk)
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
                    [1] - Título
                    [2] - Categorías
                    [3] - Ingredientes
                    [4] - Instrucciones
                    [5] - Mostrar
                    [S] - Salir
                    ------------------------------------------------------
                """.trimIndent()
            )
            print("Elige una opción: ")
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

    private fun viewRecipe() {
        println("------------------------------------------------------")
        print("Ingresar el código de la receta: ")
        val codigo = readLine().toString()
        if (codigo.isNotEmpty()) {
            try {
                val recipe = recipeBook.recipes[codigo.toInt()-1]
                showRecipe(recipe)
            } catch (exception: Exception) {
                showMessage("Debe ingresar un código de receta válido ... ")
            }
        }
    }

    private fun modifyRecipe() {
        showMessage(" Rempazar Receta ... ")
    }

    private fun removeRecipe() {
        println("------------------------------------------------------")
        print("Ingrese el código de la receta a remover: ")
        val code = readLine().toString()
        if (code.isNotEmpty()) {
            try {
                val index: Int =  code.toInt() - 1
                val recipe = recipeBook.recipes[index]
                print("Confirma remover [${recipe.title}] (Si={S/s}  o  No={N/n}): ")
                val opcion = readLine().toString()
                when (opcion) {
                    "N", "n" -> showMessage("Operación cancelada ... ")
                    "S", "s" -> {
                        recipeBook.recipes.remove(recipe)
                        //recipeBook.ingredients.removeAt(index)
                        showMessage("Se ha removido la receta ... ")
                    }
                    else -> showMessage("Las opciones válidas son {S/s} o {N/n} ... ")
                }
            } catch (exception: Exception) {
                showMessage("Debe ingresar un código de receta válido ... ")
            }
        }
    }

    private fun showRecipes() {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("RECETAS:")
            listRecipes()
            println("------------------------------------------------------")
            print("Usted desea [C]rear, [V]er, [E]ditar, [R]emover o [S]alir: ")
            val opcion = readLine().toString()

            when (opcion) {
                "C", "c" -> createRecipe()
                "V", "v" -> viewRecipe()
                "E", "e" -> modifyRecipe()
                "R", "r" -> removeRecipe()
                "S", "s" -> salir = true
                else -> showMessage("Las opciones válidas son C, V, E, B, o S ... ")
            }
        }
    }

    private fun exitApp() {
        saveData(datasource)
        showMessage("Hasta pronto ... ")
    }

    private fun showMainMenu() {
        var opcion = ""
        while (opcion != "S" && opcion != "s") {
            println(
                """
                    ------------------------------------------------------
                    MENU PRINCIPAL
                    [1] - Categorías
                    [2] - Ingredientes
                    [3] - Recetas
                    [S] - Salir
                    ------------------------------------------------------
                """.trimIndent()
            )
            print("Elige una opción: ")
            opcion = readLine().toString()
            when (opcion) {
                "1" -> showCategories()
                "2" -> showIngredients()
                "3" -> showRecipes()
                "S", "s" -> exitApp()
                else -> showMessage("Las opciones válidas son 1, 2, 3 o S ... ")
            }
        }
    }

    /*
    fun run(jsonFile: String) {
        dataSource = jsonFile
        loadData(dataSource)
        showMainMenu()
    }
     */

    override fun run() {
        loadData(datasource)
        showMainMenu()
    }
}

fun main(args: Array<String>) = RecipesMaker().main(args)