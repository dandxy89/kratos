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

  it should "display the correct fairways in regulation" in {
    val req                = makeRequestWithToken(Method.GET, "fir/12", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """{"count":13,"totalPlayed":15}"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display the correct greens in regulation" in {
    val req                = makeRequestWithToken(Method.GET, "gir/12", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """{"count":12,"totalPlayed":15}"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display holes where greens were hit in regulation" in {
    val req                = makeRequestWithToken(Method.GET, "gir/holes/12", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """[3,13]"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display holes not hit in regulation" in {
    val req                = makeRequestWithToken(Method.GET, "gir/holes/not/12", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """[5,17]"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display putts per round" in {
    val req                = makeRequestWithToken(Method.GET, "putting/round/12", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """34.0"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display putting by distance metrics" in {
    val req                = makeRequestWithToken(Method.GET, "putting/12/5/120", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """[{"distance":3.0,"averageStrokes":3.0},{"distance":6.0,"averageStrokes":2.0}]"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display average number of putts when hit in regulation" in {
    val req                = makeRequestWithToken(Method.GET, "putting/gir/12", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """1.1234"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display average number of putts when not hit in regulation" in {
    val req                = makeRequestWithToken(Method.GET, "putting/gir/not/12", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """1.23213"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display the average first putt distance when hit in regulation" in {
    val req                = makeRequestWithToken(Method.GET, "putting/first/gir/12", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """1.93213"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display the average first putt distance when not hit in regulation" in {
    val req                = makeRequestWithToken(Method.GET, "putting/first/gir/not/12", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """19.0"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display the number of penalties per round" in {
    val req                = makeRequestWithToken(Method.GET, "penalties/12", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """0.0"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display the best X shots per round" in {
    val req = makeRequestWithToken(Method.GET, "strokes_gained/shots/best/12/1", "")
    val res = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody =
      """[{"hole":10,"shot":3,"par":4,"distance":200.0,"ballLocation":2,"club":19,"strokesGained":-0.4,"strokesIndex":3}]"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display the worst X shots per round" in {
    val req = makeRequestWithToken(Method.GET, "strokes_gained/shots/worst/12/1", "")
    val res = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody =
      """[{"hole":10,"shot":3,"par":4,"distance":200.0,"ballLocation":2,"club":19,"strokesGained":1.4,"strokesIndex":3}]"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display strokes gained by club" in {
    val req                = makeRequestWithToken(Method.GET, "strokes_gained/club/12", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """[{"club":19,"minimum":-1.2,"average":-0.5,"maximum":0.2}]"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display possible strokes gained without X worst shots" in {
    val req                = makeRequestWithToken(Method.GET, "strokes_gained/without/12/5", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """-2.3"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display club distances" in { // CHECK THIS
    val req                = makeRequestWithToken(Method.GET, "club/distance/12", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """[{"club":1,"distance":321.0},{"club":2,"distance":278.0}]"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }

  it should "display driving distance" in {
    val req                = makeRequestWithToken(Method.GET, "driving/distance/12", "")
    val res                = metricsRoute(req).value.unsafeRunSync()
    val expectedResultBody = """[{"club":19,"distance":123.0},{"club":15,"distance":193.0}]"""

    validateResult(res, _.status shouldBe Status.Ok)
    validateResult(res, _.as[String].unsafeRunSync() shouldBe expectedResultBody)
  }
}
