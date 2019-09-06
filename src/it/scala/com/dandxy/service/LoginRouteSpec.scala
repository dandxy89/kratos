package com.dandxy.service

import cats.effect.IO
import cats.implicits._
import com.dandxy.jwt.GenerateToken
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.{ Password, UserEmail }
import org.http4s.{ Header, Method, Request, Status, Uri }
import org.scalatest.{ FlatSpec, Matchers }

import scala.language.higherKinds

class LoginRouteSpec extends FlatSpec with Matchers {

  behavior of "Login Route"

  def mockAttemptLogin: (UserEmail, Password) => IO[Option[PlayerId]] =
    (e, _) =>
      e match {
        case UserEmail("test@gmail.com") => IO.pure(Option(PlayerId(123)))
        case _                           => None.pure[IO]
      }

  it should "return 200 OK when the attempt login succeeds" in {
    val route = LoginRoute[IO](mockAttemptLogin, GenerateToken.prepareToken("asdasd", "TestCASE"))
    val req = Request[IO](Method.GET, Uri.unsafeFromString("golfer"))
      .withHeaders(Header("Authorization", "Basic dGVzdEBnbWFpbC5jb206dGVzdFBhc3N3b3Jk"))

    val res = route.loginRoute.run(req).value.unsafeRunSync()

    res match {
      case Some(value) => value.status shouldBe Status.Ok
      case None        => fail()
    }
  }

  it should "return 401 OK when the attempt login fails" in {
    val route = LoginRoute[IO](mockAttemptLogin, GenerateToken.prepareToken("asdasd", "TestCASE"))
    val req = Request[IO](Method.GET, Uri.unsafeFromString("golfer"))
      .withHeaders(Header("Authorization", "Basic 123123123kjasdjkansdkja"))

    val res = route.loginRoute.run(req).value.unsafeRunSync()

    res match {
      case Some(value) => value.status shouldBe Status.Unauthorized
      case None        => fail()
    }
  }
}
