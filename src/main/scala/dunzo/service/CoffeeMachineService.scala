package dunzo.service

import dunzo.DTOs.{Beverage, Ingredient}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

class CoffeeMachineService(outlets: Int) {

    private val beverageMakerService = new BeverageMakerService
    private val inventoryManagementService = new InventoryManagementService

    def executeOrders(beverages: List[String]): Future[Unit] = {
        for {
            _ <- Future.sequence((0 until outlets).foldLeft(List.empty[Future[Unit]]) { (list, index) =>
              if(index < beverages.length) orderBeverage(beverages(index)) :: list else list
            })
            _ <- checkRemaining(beverages.takeRight(beverages.length-outlets))
        } yield ()
    }

    def orderBeverage(beverageName: String): Future[Unit] = {
        beverageMakerService.makeBeverage(Beverage(beverageName))
          .map(_ => println(s"$beverageName is prepared"))
          .recover { case e: Exception =>
              println(s"$beverageName couldn't be prepared because ${e.getMessage}")
          }
    }


    def updateIngredientStock(ingredientName: String, quantity: Int): Future[Unit] = {
        inventoryManagementService.addQuantity(Ingredient.apply(ingredientName), quantity)
    }

    def checkRemaining(beverage: List[String]): Future[Unit] = {
        beverage match {
            case Nil => Future.successful(())
            case head::tail => executeOrders(head::tail)
        }
    }

}
