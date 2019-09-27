package com.dandxy.service

import cats.effect._
import com.dandxy.jwt.GenerateToken
import com.dandxy.model.player.PlayerId
import com.dandxy.testData.MockRouteTestData
import org.scalatest.{ FlatSpec, Matchers }
import org.http4s.{ Method, Request }
import org.http4s.Status
import com.dandxy.golf.input.GolfInput.UserGameInput

import scala.concurrent.ExecutionContext

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
    val expectedResultBody =
      """[{"playerId":3,"club":15,"typicalShape":null,"typicalHeight":null,"manufacturer":null,"typicalDistance":100.0,"distanceType":3}]"""

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

  it should "get all the games in the db" in {
    val req = makeRequestWithToken(Method.GET, "game/all", "")
    val res = golfingRoute(req).value.unsafeRunSync()
    val expectedResultBody =
      """[{"playerId":3,"gameStartTime":1231233123,"courseName":"Test Course","handicap":5.0,"ballUsed":null,"greenSpeed":null,"temperature":null,"windSpeed":null,"gameId":null}]"""

    res match {
      case None => fail("Did not match on route correctly")
      case Some(value) =>
        value.status shouldBe Status.Ok
        value.as[String].unsafeRunSync() shouldBe expectedResultBody
    }
  }

  it should "get specific game in the db" in {
    val req = makeRequestWithToken(Method.GET, "game/6", "")
    val res = golfingRoute(req).value.unsafeRunSync()
    val expectedResultBody =
      """[{"playerId":3,"gameStartTime":1231233123,"courseName":"Test Course","handicap":5.0,"ballUsed":null,"greenSpeed":null,"temperature":null,"windSpeed":null,"gameId":null}]"""

    res match {
      case None        => fail("Did not match on route correctly")
      case Some(value) => value.status shouldBe Status.Ok
    }
  }

  it should "get game which doesn't exist in the db" in {
    val req = makeRequestWithToken(Method.GET, "game/3", "")
    val res = golfingRoute(req).value.unsafeRunSync()

    res match {
      case None        => fail("Did not match on route correctly")
      case Some(value) => value.status shouldBe Status.Ok
    }
  }

  it should "generate an new Game Id on receipt of a PUT request" in {
    val req = makeRequestWithToken(Method.PUT, "game", addGame)
    val res = golfingRoute(req).value.unsafeRunSync()

    res match {
      case None => fail("Did not match on route correctly")
      case Some(value) =>
        println(value.as[String].unsafeRunSync())
        value.status shouldBe Status.Ok
    }
  }

  it should "delete a game by Id" in {
    val req = makeRequestWithToken(Method.DELETE, "game/1", "")
    val res = golfingRoute(req).value.unsafeRunSync()

    res match {
      case None        => fail("Did not match on route correctly")
      case Some(value) => value.status shouldBe Status.Ok
    }
  }

  it should "get handicap history" in {
    val req = makeRequestWithToken(Method.GET, "handicap", "")
    val res = golfingRoute(req).value.unsafeRunSync()

    res match {
      case None => fail("Did not match on route correctly")
      case Some(value) =>
        value.status shouldBe Status.Ok
        value.as[String].unsafeRunSync() shouldBe """[{"value":3.0,"dt":1231231221},{"value":3.1,"dt":1231231231}]"""
    }
  }

  it should "get aggregate result" in {
    val req = makeRequestWithToken(Method.GET, "aggregate/10", "")
    val res = golfingRoute(req).value.unsafeRunSync()

    res match {
      case None        => fail("Did not match on route correctly")
      case Some(value) => value.status shouldBe Status.Ok
    }
  }
}
