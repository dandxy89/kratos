package com.dandxy.service

import cats.effect._
import com.dandxy.jwt.GenerateToken
import com.dandxy.model.player.PlayerId
import com.dandxy.testData.MockRouteTestData
import org.scalatest.{FlatSpec, Matchers}
import org.http4s.{Method, Request}

import scala.concurrent.ExecutionContext
import org.http4s.Status

class GolferRoutesSpec extends FlatSpec with Matchers with MockRouteTestData {

  behavior of "Golf Routes"

  val testKey            = "test_secret_key"
  val validToken: String = GenerateToken.prepareToken(1, testKey)(PlayerId(3))

  val makeRequestWithToken: (Method, String, String) => Request[IO] = 
    makeRequest(validToken)

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val golfingRoute = GolferRoutes[IO](mockStore, testKey, mockStat).golferRoutes

  it should "get clubs to the db" in {
    val req = makeRequestWithToken(Method.GET, "club", "")
    val res = golfingRoute(req).value.unsafeRunSync()
    val expectedResultBody = """[{"playerId":3,"club":15,"typicalShape":null,"typicalHeight":null,"manufacturer":null,"typicalDistance":100.0,"distanceType":3}]"""

    res match {
      case None => fail("Did not match on route correctly")
      case Some(value) => 
        value.status shouldBe Status.Ok
        value.as[String].unsafeRunSync() shouldBe expectedResultBody
    }
  }

  it should "add clubs to the db" in {
    val req = makeRequestWithToken(Method.PUT, "club", addClubBody)
    val res = golfingRoute(req).value.unsafeRunSync()

    res match {
      case None => fail("Did not match on route correctly")
      case Some(value) => 
        value.status shouldBe Status.Ok
        value.as[String].unsafeRunSync() shouldBe "2"
    }
  }  
}
