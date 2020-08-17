package dunzo.service

import dunzo.DTOs.Beverage

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

class BeverageMakerService {

  private val inventoryManagementService: InventoryManagementService = new InventoryManagementService

  def makeBeverage(beverage: Beverage): Future[Unit] = {
    for {
      beverageConfig <- BeverageConfigurationService.getBeverageConfig(beverage)
        .map(_.getOrElse(throw new Exception(s"Ingredients configuration is not available for requested beverage ${beverage.name}")))

      _ <- Future.sequence(beverageConfig.ingredients.map { iWithQ =>
        inventoryManagementService.isStockAvailable(iWithQ.ingredient, iWithQ.quantity).map { isAvailable =>
          if(!isAvailable) throw new Exception(s"${iWithQ.ingredient.name} has insufficient stock")
        }
      })

      _ <- Future.sequence(beverageConfig.ingredients.map({ iWithQ =>
        inventoryManagementService.deductQuantity(iWithQ.ingredient, iWithQ.quantity)
      }))
    } yield ()
  }

}
