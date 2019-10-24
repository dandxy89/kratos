package com.dandxy.service

import cats.effect._
import com.dandxy.jwt.GenerateToken
import com.dandxy.model.player.PlayerId
import com.dandxy.testData.MockRouteTestData
import org.http4s.{ HttpRoutes, Method, Request, Status }
import com.dandxy.jwt.{ Claims, JwtAuthMiddleware }
import pdi.jwt.JwtAlgorithm
import org.http4s.server.AuthMiddleware

import scala.concurrent.ExecutionContext

class MetricsRouteSpec extends MockRouteTestData {

  behavior of "Metrics Routes"

  val testKey            = "test_secret_key"
  val validToken: String = GenerateToken.prepareToken(offsetHours = 1, key = testKey)(PlayerId(3))

  val makeRequestWithToken: (Method, String, String) => Request[IO] =
    makeRequest(validToken)

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val middleware: AuthMiddleware[IO, Claims] = JwtAuthMiddleware[IO, Claims](testKey, Seq(JwtAlgorithm.HS256))

  val metricsRoute: HttpRoutes[IO] = MetricsRoutes[IO](mockMetricsStore, middleware).metricsRoutes

  it should "successfully return the standard metrics a golfer needs - how did I do..." in {
    val req = makeRequestWithToken(Method.GET, "standard/12", "")
    val res = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody =
      """[{"eagles":1.0,"birdies":3.0,"pars":5.0,"bogeys":5.0,"doubles":2.0,"triples":1.0,"others":0.0}]"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }
}
