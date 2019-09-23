package com.dandxy.service

import cats.effect._
import com.dandxy.jwt.GenerateToken
import com.dandxy.model.player.PlayerId
import com.dandxy.testData.MockRouteTestData
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.ExecutionContext

class GolferRoutesSpec extends FlatSpec with Matchers with MockRouteTestData {

  behavior of "Golf Routes"

  val testKey            = "test_secret_key"
  val validToken: String = GenerateToken.prepareToken(1, testKey)(PlayerId(1))

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val golfingRoute = GolferRoutes[IO](mockStore, testKey, mockStat).golferRoutes

  it should "add clubs to the db" in {
    pending
  }
}
