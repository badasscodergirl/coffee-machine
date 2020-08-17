package dunzo

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CoffeeMachineAppSpec extends AnyFlatSpec with Matchers {

  "The CoffeeMachineApp object" should "mocha is prepared\nSuccessfully executed the process" in {

    val input = "testinput.json"

    CoffeeMachineApp.main(Array(input))


  }
}
