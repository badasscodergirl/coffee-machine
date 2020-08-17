package dunzo.service

import dunzo.DTOs.{Beverage, BeverageConfig, Ingredient, IngredientWithQuantity}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

object BeverageConfigurationService {

  private var beveragesConfig: Map[Beverage, BeverageConfig] = Map.empty[Beverage, BeverageConfig]

  def addBeverageConfig(beverageData: Map[String, Seq[(String, Int)]]): Future[Unit] = Future {
    val config = beverageData.keys.map { bName =>
      val beverage = Beverage(bName.trim)
      val ingredients = beverageData(bName)
      val ingredientsWithQuantity = ingredients.map { case (i, q) =>
        IngredientWithQuantity(Ingredient.apply(i), q)
      }
      (beverage, BeverageConfig(beverage, ingredientsWithQuantity))
    }
    beveragesConfig ++= config.toMap
  }

  def getBeverageConfig(beverage: Beverage): Future[Option[BeverageConfig]] = Future {
    beveragesConfig.get(beverage)
  }

  def getAllBeverageNames(): Set[Beverage] = beveragesConfig.keySet

}
