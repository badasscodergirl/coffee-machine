package dunzo

import dunzo.parser.InputParser
import dunzo.service.{BeverageConfigurationService, CoffeeMachineService, InventoryManagementService}

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits._


object CoffeeMachineApp extends App {

  private val inputParser = new InputParser
  private val inventoryManagementService = new InventoryManagementService
  private def getCoffeeMachineService(outlets: Int): CoffeeMachineService = new CoffeeMachineService(outlets)

  private lazy val defaultInputFile = "input1.json"

  def init(): Unit =  {
    val fileName = if(args.isEmpty) defaultInputFile else args(0)
    startProcess(fileName).onComplete {
      case Success(_) => println(s"Successfully executed the process")
      case Failure(error) => //println(s"Failed to execute the process due to error ${error.getMessage}")
      throw error
    }
  }

  def startProcess(fileName: String): Future[Unit] = for {
    inputJson <- inputParser.readInput(fileName)

    outlets = inputParser.extractOutlets(inputJson)
    itemQuantities = inputParser.extractItemQuantity(inputJson)
    beverageConfigData = inputParser.extractBeverageConfig(inputJson)

    beverages = beverageConfigData.keySet.toList

    coffeeMachineService = getCoffeeMachineService(outlets)
    _ <-inventoryManagementService.addQuantityForItems(itemQuantities)
    _ <- BeverageConfigurationService.addBeverageConfig(beverageConfigData)

    _ <- coffeeMachineService.executeOrders(beverages)

  } yield ()


  init()
}
