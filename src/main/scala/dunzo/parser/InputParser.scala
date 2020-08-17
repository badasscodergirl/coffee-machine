package dunzo.parser

import play.api.libs.json.{JsObject, Json}

import scala.concurrent.Future
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits._

class InputParser {


  def readInput(fileName: String): Future[JsObject] = Future {
      val jsonStr =  Source.fromResource(fileName).getLines.mkString
      val jsonObj = Json.parse(jsonStr)
      (jsonObj \ "machine").as[JsObject]
  }


  def extractOutlets(machineInput: JsObject): Int = (machineInput \ "outlets" \ "count_n").as[Int]

  def extractItemQuantity(machineInput: JsObject): List[(String, Int)] =  {
    val itemsQuantity = (machineInput \ "total_items_quantity").as[JsObject]

    itemsQuantity.fields.map { case (key, jsValue) =>
      (key, jsValue.as[Int])
    }.toList
  }

  def extractBeverageConfig(machineInput: JsObject): Map[String, List[(String, Int)]] = {
    (machineInput \ "beverages").as[JsObject].fields.map { case (bevName, configData) =>
      val ingredients: List[(String, Int)] = configData.as[JsObject].fields.map { case (ingr, quantJsValue) =>
        (ingr, quantJsValue.as[Int])
      }.toList
      (bevName, ingredients)
    }.toMap
  }

}
