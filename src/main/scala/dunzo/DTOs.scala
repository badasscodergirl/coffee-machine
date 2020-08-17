package dunzo

import play.api.libs.json.{Format, Json}

object DTOs {
  case class Ingredient(name: String)

  object Ingredient {
    def apply(name: String): Ingredient = new Ingredient(name.toLowerCase)
    implicit val formats: Format[Ingredient] = Json.format[Ingredient]
  }

  case class IngredientWithQuantity(ingredient: Ingredient, quantity: Int)

  case class Beverage(name: String)

  object Beverage {
    def apply(name: String): Beverage = new Beverage(name.toLowerCase)

    implicit val formats: Format[Beverage] = Json.format[Beverage]
  }

  case class BeverageConfig(beverage: Beverage, ingredients: Seq[IngredientWithQuantity])

}
