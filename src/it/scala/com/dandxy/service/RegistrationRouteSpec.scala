package com.dandxy.service

import java.sql.Timestamp

import cats.effect.IO
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.{Password, UserRegistration}
import io.circe.Json
import io.circe.parser.parse

import org.http4s.{Header, HttpRoutes, Method, Request, Status, Uri}
import org.scalatest.{FlatSpec, Matchers}

class RegistrationRouteSpec extends FlatSpec with Matchers {

 behavior of "Registration Route"

 val validBody = """{"user":{"email":"ds@gmail.com","firstName":"d","lastName":"g"},"password":"asd123"}"""

 it should "successfully register a new player" in {
   val mockRegistration: (UserRegistration, Password, Timestamp) => IO[PlayerId] =
     (_, _, _) => IO.pure(PlayerId(123))

   val route: HttpRoutes[IO] = RegistrationRoute(mockRegistration).registrationRoutes

   val req = Request[IO](Method.POST, Uri.unsafeFromString("golfer"))
     .withEntity(validBody)
     .withHeaders(Header("Accept", "application/json"), Header("Content-Type", "application/json"))

   route.run(req).value.unsafeRunSync() match {
     case Some(value) => value.status shouldBe Status.Ok
     case None        => fail("Did not get a response from the server")
   }
 }

 it should "return a correct error if unsuccessful" in {
   val mockRegistration: (UserRegistration, Password, Timestamp) => IO[PlayerId] =
     (_, _, _) => IO.raiseError(new Throwable("Ka Boom!"))

   val route: HttpRoutes[IO] = RegistrationRoute(mockRegistration).registrationRoutes

   val req = Request[IO](Method.POST, Uri.unsafeFromString("golfer"))
     .withEntity(validBody)
     .withHeaders(Header("Accept", "application/json"), Header("Content-Type", "application/json"))

   route.run(req).value.unsafeRunSync() match {
     case Some(value) => value.status shouldBe Status.BadRequest
     case None        => fail("Did not get a response from the server")
   }
 }
}
