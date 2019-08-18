package com.dandxy.service

import cats.effect.IO
import org.http4s.{ Header, Headers, HttpVersion, Method, Request, Uri }
import org.scalatest.{ FlatSpec, Matchers }

class HealthRoutesSpec extends FlatSpec with Matchers {

  behavior of "Health Routes"

  val endpointHealth: HealthRoutes[IO] = HealthRoutes[IO]()

  it should "respond with Pong" in {
    val request = Request[IO](Method.GET, Uri.unsafeFromString("/ping"))
      .withHeaders(Headers.of(Header("Accept", "text/plain")))

    val resp = endpointHealth.pingPongService
      .run(request)
      .value
      .unsafeRunSync()

    resp match {
      case Some(value) => value.as[String].unsafeRunSync() shouldBe "Pong"
      case None        => fail("Failed to get correct response from ping route")
    }
  }
}
