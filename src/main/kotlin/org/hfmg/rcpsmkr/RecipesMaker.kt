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
            groups = mutableListOf<Group>(),
            ingredients = mutableListOf<Ingredient>(),
            categories =mutableListOf<Category>(),
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

    private fun listGroups() {
        if (recipeBook.groups.isNotEmpty()) {
            for (group in recipeBook.groups) {
                val index = recipeBook.groups.indexOf(group)
                println("[${index+1}] - ${group.name}")
            }
        } else {
            println("<--- Sin Grupos --->")
        }
    }

    private fun listIngredients() {
        if (recipeBook.ingredients.isNotEmpty()) {
            for (ingredient in recipeBook.ingredients) {
                val index = recipeBook.ingredients.indexOf(ingredient)
                println("[${index+1}] - ${ingredient.name} (${ingredient.group.name})")
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

    private fun listRecipeCategories1(recipe: Recipe) {
        if (recipe.categories.isNotEmpty()) {
            for (category in recipe.categories) {
                val index = recipe.categories.indexOf(category)
                println("${index+1}. ${category.name}")
            }
        } else {
            println("<--- Sin definir categorías --->")
        }
    }

    private fun listRecipeCategories2(recipe: Recipe) {
        if (recipe.categories.isNotEmpty()) {
            var categories = ""
            for (category in recipe.categories) {
                val index = recipe.categories.indexOf(category)
                categories += category.name
                if (index+1 < recipe.categories.size) {
                    categories += ", "
                }
            }
            println("($categories)")
        } else {
            println("<--- Sin definir categorías --->")
        }
    }

    private fun listRecipeIngredients(recipe: Recipe) {
        if (recipe.ingredients.isNotEmpty()) {
            for (ingredientRecipe in recipe.ingredients) {
                val index = recipe.ingredients.indexOf(ingredientRecipe)
                println("${index+1}. ${ingredientRecipe.ingredient.name} (${ingredientRecipe.cantidad})")
            }
        } else {
            println("<--- Sin definir ingredientes --->")
        }
    }

    private fun listRecipeInstructions(recipe: Recipe) {
        if (recipe.instructions.isNotEmpty()) {
            for (instruction in recipe.instructions) {
                val index = recipe.instructions.indexOf(instruction)
                println("${index+1}. ${instruction}. ")
            }
        } else {
            println("<--- Sin definir instrucciones --->")
        }
    }

    private fun createGroup() {
        println("------------------------------------------------------")
        print("Ingresar el nombre del nuevo grupo: ")
        val name = readLine().toString()
        if (name != "") {
            recipeBook.groups.add(Group(name))
            recipeBook.groups.sortBy { it.name }
            showMessage("Se creo el nuevo grupo ... ")
        } else {
            showMessage("No ingreso ningun nombre ... ")
        }
    }

    private fun modifyGroup() {
        println("------------------------------------------------------")
        print("Ingrese el código del grupo a modificar: ")
        val code = readLine().toString()
        if (code.isNotEmpty()) {
            try {
                val index: Int =  code.toInt() - 1
                val group = recipeBook.groups[index]
                print("Cambiar [${group.name}] por el nuevo nombre: ")
                val name = readLine().toString()
                if (name != "") {
                    group.name = name
                    //recipeBook.groups[index] = category
                    recipeBook.groups.set(index, group)
                    recipeBook.groups.sortBy { it.name }
                    showMessage("Se ha cambiado el nombre del grupo ... ")
                } else {
                    showMessage("No ingreso ningun nombre ... ")
                }
            } catch (exception: Exception) {
                showMessage("Debe ingresar un código de grupo válido ... ")
            }
        }
    }

    private fun removeGroup() {
        println("------------------------------------------------------")
        print("Ingrese el código del grupo a remover: ")
        val code = readLine().toString()
        if (code.isNotEmpty()) {
            try {
                val index: Int =  code.toInt() - 1
                val grupo = recipeBook.groups[index]
                print("Confirma remover [${grupo.name}] (Si={S/s}  o  No={N/n}): ")
                val opcion = readLine().toString()
                when (opcion) {
                    "N", "n" -> showMessage("Operación cancelada ... ")
                    "S", "s" -> {
                        recipeBook.groups.remove(grupo)
                        //recipeBook.groups.removeAt(index)
                        showMessage("Se ha removido el grupo ... ")
                    }
                    else -> showMessage("Las opciones válidas son {S/s} o {N/n} ... ")
                }
            } catch (exception: Exception) {
                showMessage("Debe ingresar un código de grupo válido ... ")
            }
        }
    }

    private fun showGroups() {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("GRUPOS DE INGREDIENTES:")
            listGroups()
            println("------------------------------------------------------")
            print("Usted desea [C]rear, [M]odificar, [R]emover o [S]alir: ")
            val opcion = readLine().toString()

            when (opcion) {
                "C", "c" -> createGroup()
                "M", "m" -> modifyGroup()
                "R", "r" -> removeGroup()
                "S", "s" -> salir = true
                else -> showMessage("Las opciones válidas son C, M, R, o S ... ")
            }
        }
    }

    private fun createIngredient() {
        println("------------------------------------------------------")
        println("GRUPOS DE INGREDIENTES:")
        listGroups()
        println("------------------------------------------------------")
        print("Seleccione el grupo del nuevo ingrediente: ")
        val code = readLine().toString()
        if (code != "") {
            try {
                val grupo = recipeBook.groups[code.toInt()-1]
                print("Ingresar el nombre del nuevo ingrediente: ")
                val name = readLine().toString()
                if (name != "") {
                    recipeBook.ingredients.add(Ingredient(name, grupo))
                    recipeBook.ingredients.sortBy { it.name }
                    showMessage("Se ha creado el nuevo ingrediente ... ")
                } else {
                    showMessage("No ingreso ningun nombre ... ")
                }
            } catch (exception: Exception) {
                showMessage("Debe ingresar un código de grupo válido ... ")
            }
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

    private fun searchRecipe(titulo: String): Boolean {
        var encontrada = false
        for (recipe in recipeBook.recipes) {
            encontrada = recipe.title == titulo
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

    private fun addRecipeCategory(recipe: Recipe): Recipe {
        println("\n------------------------------------------------------")
        println("CATEGORIAS DISPONIBLES:")
        listCategories()
        println("------------------------------------------------------")
        print("Elegir un código de categoría [?]: ")
        val code = readLine().toString()
        if (code != "") {
            try {
                val category = recipeBook.categories[code.toInt()-1]
                recipe.categories.add(category)
                recipe.categories.sortedBy { it.name }
            } catch (exception: Exception) {
                showMessage("Debe ingresar un código de categoría válido ... ")
            }
        } else {
            showMessage("Debe ingresar un código de categoría válido ... ")
        }
        return recipe
    }

    private fun removeRecipeCategory(recipe: Recipe): Recipe {
        println("------------------------------------------------------")
        print("Ingrese el código de la categoría a remover: ")
        val code = readLine().toString()
        if (code.isNotEmpty()) {
            try {
                val index: Int =  code.toInt() - 1
                val recipeCategory = recipe.categories.elementAt(index)
                print("Remover [${index+1}. ${recipeCategory.name}] (Si={S/s}  o  No={N/n}): ")
                val opcion = readLine().toString()
                when (opcion) {
                    "N", "n" -> showMessage("Operación cancelada ... ")
                    "S", "s" -> {
                        val category = recipe.categories.elementAt(index)
                        recipe.categories.remove(category)
                        showMessage("Se ha removido la categoría ... ")
                    }
                    else -> showMessage("Las opciones válidas son {S/s} o {N/n} ... ")
                }
            } catch (exception: Exception) {
                showMessage("Debe ingresar un código de ingrediente válido ... ")
            }
        }
        return recipe
    }

    private fun inputCategories(rcp: Recipe): Recipe {
        var recipe = rcp
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("CATEGORIAS DE LA RECETA:")
            listRecipeCategories1(recipe)
            println("------------------------------------------------------")
            print("Usted desea [A]ñadir, [R]emover o [S]alir: ")
            val opcion = readLine().toString()

            when (opcion) {
                "A", "a" -> recipe =  addRecipeCategory(recipe)
                "R", "r" -> recipe = removeRecipeCategory(recipe)
                "S", "s" -> salir = true
                else -> showMessage("Las opciones válidas son A, R, o S ... ")
            }
        }
        return recipe
    }

    private fun containsText(texto1: String, texto2: String): Boolean {
        return texto1.toUpperCase().contains(texto2.toUpperCase())
    } 

    private fun addRecipeIngredient(recipe: Recipe): Recipe {
        println("------------------------------------------------------")
        print("Ingresar ingrediente a buscar (o Cero para salir): ")
        var texto = readLine().toString()
        if (texto.isNotEmpty()) {
            texto = texto.toUpperCase()
            val ingredientsFound = recipeBook.ingredients.filter { containsText(it.name, texto) }
            if (ingredientsFound.isNotEmpty()) {
                println("INGREDIENTES ENCONTRADOS:")
                for (ingredient in ingredientsFound) {
                    val index = ingredientsFound.indexOf(ingredient)
                    println("${index+1}. ${ingredient.name}")
                }
                println("------------------------------------------------------")
                print("Elegir un código de ingrediente [?]: ")
                val opcion = readLine().toString()
                if (opcion != "") {
                    try {
                        val ingredient = ingredientsFound[opcion.toInt()-1]
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
        } else {
            showMessage("Usted no ingreso ningun texto ... ")
        }
        return recipe
    }

    private fun removeRecipeIngredient(recipe: Recipe): Recipe {
        println("------------------------------------------------------")
        print("Ingrese el código del ingrediente a remover: ")
        val code = readLine().toString()
        if (code.isNotEmpty()) {
            try {
                val index: Int =  code.toInt() - 1
                val recipeIngredient = recipe.ingredients[index]
                val name = recipeIngredient.ingredient.name
                val quantity = recipeIngredient.cantidad
                print("Remover [$name ($quantity)] (Si={S/s}  o  No={N/n}): ")
                val opcion = readLine().toString()
                when (opcion) {
                    "N", "n" -> showMessage("Operación cancelada ... ")
                    "S", "s" -> {
                        recipe.ingredients.remove(recipeIngredient)
                        showMessage("Se ha removido el ingrediente ... ")
                    }
                    else -> showMessage("Las opciones válidas son {S/s} o {N/n} ... ")
                }
            } catch (exception: Exception) {
                showMessage("Debe ingresar un código de ingrediente válido ... ")
            }
        }
        return recipe
    }

    private fun inputIngredients(rcp: Recipe): Recipe {
        var recipe = rcp
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("INGREDIENTES DE LA RECETA:")
            listRecipeIngredients(recipe)
            println("------------------------------------------------------")
            print("Usted desea [A]ñadir, [R]emover o [S]alir: ")
            val opcion = readLine().toString()

            when (opcion) {
                "A", "a" -> recipe = addRecipeIngredient(recipe)
                "R", "r" -> recipe = removeRecipeIngredient(recipe)
                "S", "s" -> salir = true
                else -> showMessage("Las opciones válidas son A, R, o S ... ")
            }
        }
        return recipe
    }

    private fun addRecipeInstruction(recipe: Recipe): Recipe {
        println("\n------------------------------------------------------")
        println("Ingresar una acción para elaboración de la receta: ")
        val paso = readLine().toString()
        if (paso.isNotEmpty()) {
            recipe.instructions.add(paso)
            showMessage("La acción fue añadida ... ")
        } else {
            showMessage("Debe ingresar un paso no vacio ... ")
        }
        return recipe
    }

    private fun removeRecipeInstruction(recipe: Recipe): Recipe {
        println("------------------------------------------------------")
        print("Ingrese el código del paso a remover: ")
        val code = readLine().toString()
        if (code.isNotEmpty()) {
            try {
                val index: Int =  code.toInt() - 1
                val paso = recipe.instructions[index]
                print("Remover [${index+1}. $paso] (Si={S/s}  o  No={N/n}): ")
                val opcion = readLine().toString()
                when (opcion) {
                    "N", "n" -> showMessage("Operación cancelada ... ")
                    "S", "s" -> {
                        recipe.instructions.removeAt(index)
                        showMessage("Se ha removido el paso ... ")
                    }
                    else -> showMessage("Las opciones válidas son {S/s} o {N/n} ... ")
                }
            } catch (exception: Exception) {
                showMessage("Debe ingresar un código de paso válido ... ")
            }
        }
        return recipe
    }

    private fun inputInstructions(rcp: Recipe): Recipe {
        var recipe = rcp
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("INSTRUCCIONES DE PREPARACION:")
            listRecipeInstructions(recipe)
            println("------------------------------------------------------")
            print("Usted desea [A]ñadir, [R]emover o [S]alir: ")
            val opcion = readLine().toString()

            when (opcion) {
                "A", "a" -> recipe = addRecipeInstruction(recipe)
                "R", "r" -> recipe = removeRecipeInstruction(recipe)
                "S", "s" -> salir = true
                else -> showMessage("Las opciones válidas son A, R, o S ... ")
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
        //listRecipeCategories1(recipe)
        listRecipeCategories2(recipe)
        println("INGREDIENTES:")
        listRecipeIngredients(recipe)
        println("INSTRUCCIONES:")
        listRecipeInstructions(recipe)
        println("..................................................................................")
        showMessage("Mostrando Receta ... ")
    }

    private fun validateRecipe(recipe: Recipe): Boolean {
        val titleOk = !recipe.title.isEmpty()
        val categoriesOk = recipe.categories.isNotEmpty()
        val ingredientsOk = recipe.ingredients.isNotEmpty()
        val instructionsOk = !recipe.instructions.isNullOrEmpty()
        //val validado = (titleOk && categoriesOk && ingredientsOk && instructionsOk)
        val validado = (titleOk)
        if (!validado) {
            showMessage("La receta no tiene [Título] ... ")
        }
        return validado
    }

    private fun saveRecipe(recipe: Recipe, newRecipe: Boolean, index: Int = -1): Boolean {
        var salir = false
        var guardada = false
        while (!salir) {
            print("Usted guarda la receta (Si={S/s}  o  No={N/n}: ")
            val opcion = readLine().toString()
            when (opcion) {
                "N","n" -> salir = true
                "S","s" -> {
                    if (validateRecipe(recipe)) {
                        if (newRecipe) {
                            recipeBook.recipes.add(recipe)
                        } else {
                            recipeBook.recipes.set(index, recipe)
                        }
                        recipeBook.recipes.sortBy { it.title }
                        guardada = true
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

    private fun editRecipe(rcp: Recipe?, index: Int = -1) {
        var newRecipe: Boolean
        var recipe = Recipe("",
            categories = mutableSetOf<Category>(),
            ingredients =mutableListOf<RecipeIngredient>(),
            instructions = mutableListOf<String>()
        )
        if (rcp != null) {
            recipe = rcp
            newRecipe = false
        } else {
            newRecipe = true
        }

        var salir = false
        while (!salir) {
            println(
                """
                    ------------------------------------------------------
                    RECETA:
                    [1] - Título
                    [2] - Categorías
                    [3] - Ingredientes
                    [4] - Preparación
                    [5] - Mostrar
                    [S] - Salir
                    ------------------------------------------------------
                """.trimIndent()
            )
            print("Elige una opción: ")
            val opcion = readLine().toString()
            when (opcion) {
                "1" -> recipe = inputName(recipe)
                "2" -> recipe = inputCategories(recipe)
                "3" -> recipe = inputIngredients(recipe)
                "4" -> recipe = inputInstructions(recipe)
                "5" -> showRecipe(recipe)
                "S", "s" -> {
                    saveRecipe(recipe, newRecipe, index)
                    salir = true
                }
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
        println("------------------------------------------------------")
        print("Ingresar el código de la receta a modificar: ")
        val code = readLine().toString()
        if (code.isNotEmpty()) {
            try {
                val index = code.toInt()-1
                val recipe = recipeBook.recipes[index]
                editRecipe(recipe, index)
            } catch (exception: Exception) {
                showMessage("Debe ingresar un código de receta válido ... ")
            }
        }
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
            print("Usted desea [C]rear, [V]er, [M]odificar, [R]emover o [S]alir: ")
            val opcion = readLine().toString()

            when (opcion) {
                "C", "c" -> editRecipe(null)
                "V", "v" -> viewRecipe()
                "M", "m" -> modifyRecipe()
                "R", "r" -> removeRecipe()
                "S", "s" -> salir = true
                else -> showMessage("Las opciones válidas son C, V, M, B, o S ... ")
            }
        }
    }

    private fun exitApp() {
        saveData(datasource)
        showMessage("\nH A S T A   P R O N T O\n\n ... ")
    }

    private fun showMainMenu() {
        var opcion = ""
        while (opcion != "S" && opcion != "s") {
            println(
                """
                    ------------------------------------------------------
                    MENU PRINCIPAL                    
                    [1] - Grupos de Ingredientes
                    [2] - Ingredientes
                    [3] - Categorías de Recetas
                    [4] - Recetas
                    [S] - Salir
                    ------------------------------------------------------
                """.trimIndent()
            )
            print("Elige una opción: ")
            opcion = readLine().toString()
            when (opcion) {
                "1" -> showGroups()
                "2" -> showIngredients()
                "3" -> showCategories()
                "4" -> showRecipes()
                "S", "s" -> exitApp()
                else -> showMessage("Las opciones válidas son 1, 2, 3, 4 o S ... ")
            }
        }
    }

    override fun run() {
        loadData(datasource)
        showMainMenu()
    }
}

fun main(args: Array<String>) = RecipesMaker().main(args)