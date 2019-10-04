package com.dandxy.service

import java.sql.Timestamp

import cats.effect.IO
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.{ Password, UserRegistration }
import com.dandxy.testData.MockRouteTestData
import org.http4s.{ Header, HttpRoutes, Method, Request, Status, Uri }

class RegistrationRouteSpec extends MockRouteTestData {

  behavior of "Registration Route"

  val validBody = """{"user":{"email":"ds@gmail.com","firstName":"d","lastName":"g"},"password":"asd123"}"""

  it should "successfully register a new player" in {
    val mockRegistration: (UserRegistration, Password, Timestamp) => IO[PlayerId] =
      (_, _, _) => IO.pure(PlayerId(123))

    val route: HttpRoutes[IO] = RegistrationRoute(mockRegistration).registrationRoutes

    val req = Request[IO](Method.POST, Uri.unsafeFromString("golfer"))
      .withEntity(validBody)
      .withHeaders(Header("Accept", "application/json"), Header("Content-Type", "application/json"))

    validateResult(route.run(req).value.unsafeRunSync(), _.status shouldBe Status.Ok)
  }

  it should "return a correct error if unsuccessful" in {
    val mockRegistration: (UserRegistration, Password, Timestamp) => IO[PlayerId] =
      (_, _, _) => IO.raiseError(new Throwable("Ka Boom!"))

    val route: HttpRoutes[IO] = RegistrationRoute(mockRegistration).registrationRoutes

    val req = Request[IO](Method.POST, Uri.unsafeFromString("golfer"))
      .withEntity(validBody)
      .withHeaders(Header("Accept", "application/json"), Header("Content-Type", "application/json"))

    validateResult(route.run(req).value.unsafeRunSync(), _.status shouldBe Status.BadRequest)
  }
}
