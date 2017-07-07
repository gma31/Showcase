import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.scenario.Simulation

class FlightInfoSimulation extends Simulation {
  var host = "127.0.0.1"

  var appPort = 8080
  val httpProtocol = http.baseURL("http://" + host + ":" + appPort)

  object Airline {
    val feeder = csv("airlines.csv").random

    val search = feed(feeder)
      .exec(http("all airlines").get("/airlines"))
      .exec(http("airline by iata code").get("/airlines/${ICAO}"))
      .exec(http("airlines suggestions").get("/airlines/suggestions?term=${Name}"))
  }

  object Airport {
    val feeder = csv("airports.csv").random
    val search = feed(feeder)
      .exec(http("airports by iata code").get("/airports/${IATA}"))
      .exec(http("airports suggestions").get("/airports/suggestions?term=${Name}"))
      .exec(http("all airports").get("/airports"))
  }

  object Connection {
    val feeder = csv("flightconnections.csv").random

    val search = feed(feeder)
      .exec(http("specific connection").get("/connections?from=${Source}&&to=${Destination}"))
  }

  val users = scenario("Users").exec(Airline.search,Airport.search,Connection.search)
  setUp(
    users.inject(rampUsers(100) over (10)).protocols(httpProtocol))
}