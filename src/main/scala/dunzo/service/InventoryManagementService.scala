package dunzo.service

import dunzo.DTOs.Ingredient

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

class InventoryManagementService {

  def addQuantityForItems(itemQuantities: List[(String, Int)]): Future[Unit] = {
    Future.sequence(itemQuantities.map { case (itemName, quantity) =>
      addQuantity(Ingredient.apply(itemName), quantity)
    }).map(_ => ())
  }

  def addQuantity(ingredient: Ingredient, quantity: Int): Future[Unit] = Future {
    val prevQuantity = Inventory.stock.getOrElse(ingredient, 0)
    Inventory.stock = Inventory.stock.updated(ingredient, prevQuantity+quantity)
  }

  def deductQuantity(ingredient: Ingredient, quantity: Int): Future[Unit] = Future {
    val existingQuantity = Inventory.stock.get(ingredient)
    existingQuantity match {
      case Some(exisitingQ) =>
        if(quantity > exisitingQ)
          throw new Exception(s"Trying to deduct more quantity $quantity than existing" +
            s" $exisitingQ for ingredient ${ingredient.name}")

        Inventory.stock = Inventory.stock.updated(ingredient, exisitingQ - quantity)
      case None => throw new Exception("Item not present in inventory")
    }

  }

  def doesItemExists(ingredient: Ingredient): Future[Boolean] = Future.successful(Inventory.stock.contains(ingredient))

  def isStockAvailable(ingredient: Ingredient): Future[Boolean] = isStockAvailable(ingredient, 1)


  def isStockAvailable(ingredient: Ingredient, quantity: Int): Future[Boolean] = doesItemExists(ingredient).map {
      case true => val existingQuantity = Inventory.stock.getOrElse(ingredient, 0)
        existingQuantity >= quantity
      case false => throw new Exception(s"${ingredient.name} is not available")
    }


}

object Inventory {
  var stock: Map[Ingredient, Int] = Map[Ingredient, Int]()
}