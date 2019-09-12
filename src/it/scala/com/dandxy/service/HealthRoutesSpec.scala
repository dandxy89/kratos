package com.dandxy.service

import cats.effect.IO
import cats.effect.concurrent.Ref
import com.dandxy.db.util.HealthCheck.{ OK, Status, Warning }
import com.dandxy.util.PostgresDockerService
import org.http4s.{ EntityDecoder, Header, Headers, Method, Request, Response, Uri }
import org.scalatest.{ Assertion, FlatSpec, Matchers }

class HealthRoutesSpec extends FlatSpec with Matchers {

  val service = new PostgresDockerService(5450)

  behavior of "Health Routes"

  def endpointHealth(dbStatus: IO[Ref[IO, Status]]): HealthRoutes[IO] =
    HealthRoutes[IO](dbStatus.unsafeRunSync())

  def responseMatcher[A](r: Option[Response[IO]], expected: A)(implicit ee: EntityDecoder[IO, A]): Assertion =
    r match {
      case Some(value) => value.as[A].unsafeRunSync() shouldBe expected
      case None        => fail("Failed to get correct response from route")
    }

  val goodHealth: IO[Ref[IO, Status]] = Ref[IO].of[Status](OK)
  val badHealth: IO[Ref[IO, Status]]  = Ref[IO].of[Status](Warning)

  it should "respond with Pong" in {
    val request = Request[IO](Method.GET, Uri.unsafeFromString("/ping"))
      .withHeaders(Headers.of(Header("Accept", "text/plain")))

    val resp = endpointHealth(goodHealth)
      .healthService
      .run(request)
      .value

    responseMatcher[String](resp.unsafeRunSync(), "Pong")
  }

  it should "respond correctly when checking the db status" in {
    val request = Request[IO](Method.GET, Uri.unsafeFromString("/db/status"))
      .withHeaders(Headers.of(Header("Accept", "text/plain")))

    val goodResp = endpointHealth(goodHealth)
      .healthService
      .run(request)
      .value

    val badResp = endpointHealth(badHealth)
      .healthService
      .run(request)
      .value

    responseMatcher[String](goodResp.unsafeRunSync(), """{"postgres":{"status":"Ok"}}""")
    responseMatcher[String](badResp.unsafeRunSync(), """{"postgres":{"status":"Warning"}}""")
  }
}
