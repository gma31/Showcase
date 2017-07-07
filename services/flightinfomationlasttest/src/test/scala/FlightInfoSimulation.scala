import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.scenario.Simulation

class FlightInfoSimulation extends Simulation {
  var host = "127.0.0.1"

  var appPort = 8080
  val httpProtocol = http.baseURL("http://" + host + ":" + appPort)

  object Airline {
    val search = exec(http("all airlines").get("/apitest/airlines"))

  }

  object Airport {
    val search =exec(http("specific airport").get("/airports/FRA"))
      .exec(http("airports suggestions").get("/airports/suggestions?term=Germany"))
  }

  object Connection {
    val search = exec(http("specific connection").get("/connections?from=FRA&&to=LAX"))

  }

  val users = scenario("Users").exec(Airline.search, Airport.search)
  setUp(
    users.inject(rampUsers(100) over (10)).protocols(httpProtocol))
}