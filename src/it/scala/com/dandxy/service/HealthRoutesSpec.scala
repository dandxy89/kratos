package com.dandxy.service

import cats.effect.IO
import cats.effect.concurrent.Ref
import com.dandxy.db.util.HealthCheck.{ OK, Status, Warning }
import com.dandxy.testData.MockRouteTestData
import com.dandxy.util.PostgresDockerService
import org.http4s.{ Header, Headers, Method, Request, Uri }

class HealthRoutesSpec extends MockRouteTestData {

  val service = new PostgresDockerService(5450)

  behavior of "Health Routes"

  def endpointHealth(dbStatus: IO[Ref[IO, Status]]): HealthRoutes[IO] = HealthRoutes[IO](dbStatus.unsafeRunSync())

  val goodHealth: IO[Ref[IO, Status]] = Ref[IO].of[Status](OK)
  val badHealth: IO[Ref[IO, Status]]  = Ref[IO].of[Status](Warning)

  it should "respond with Pong" in {
    val request = Request[IO](Method.GET, Uri.unsafeFromString("/ping"))
      .withHeaders(Headers.of(Header("Accept", "text/plain")))

    val resp = endpointHealth(goodHealth)
      .healthService
      .run(request)
      .value

    validateResult(resp.unsafeRunSync(), _.as[String].unsafeRunSync() shouldBe """"Pong"""")
  }

  it should "respond correctly when checking the db status" in {
    val request = Request[IO](Method.GET, Uri.unsafeFromString("/db/status"))
      .withHeaders(Headers.of(Header("Accept", "text/plain")))

    val goodResp = endpointHealth(goodHealth)
      .healthService
      .run(request)
      .value

    validateResult(goodResp.unsafeRunSync(), _.as[String].unsafeRunSync() shouldBe """{"postgres":{"status":"Ok"}}""")

    val badResp = endpointHealth(badHealth)
      .healthService
      .run(request)
      .value

    validateResult(badResp.unsafeRunSync(), _.as[String].unsafeRunSync() shouldBe """{"postgres":{"status":"Warning"}}""")
  }
}
