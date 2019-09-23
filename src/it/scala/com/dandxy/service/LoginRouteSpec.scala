package com.dandxy.service

import cats.effect.IO
import com.dandxy.jwt.GenerateToken
import com.dandxy.testData.MockRouteTestData
import org.http4s.util.CaseInsensitiveString
import org.http4s.{ Header, Method, Request, Status, Uri }
import org.scalatest.{ FlatSpec, Matchers }

import scala.language.higherKinds

class LoginRouteSpec extends FlatSpec with Matchers with MockRouteTestData {

  behavior of "Login Route"

  it should "return 200 OK when the attempt login succeeds" in {
    val route = LoginRoute[IO](mockAttemptLogin, GenerateToken.prepareToken(1, "test_secret_key"))
    val req = Request[IO](Method.GET, Uri.unsafeFromString("golfer"))
      .withHeaders(Header("Authorization", "Basic dGVzdEBnbWFpbC5jb206dGVzdFBhc3N3b3Jk"))

    val res = route.loginRoute.run(req).value.unsafeRunSync()

    res match {
      case Some(value) =>
        value.status shouldBe Status.Ok
        value.headers.get(CaseInsensitiveString("Authorization")).isEmpty shouldBe false
      case None => fail()
    }
  }

  it should "return 401 OK when the attempt login fails" in {
    val route = LoginRoute[IO](mockAttemptLogin, GenerateToken.prepareToken(1, "asdasd"))
    val req = Request[IO](Method.GET, Uri.unsafeFromString("golfer"))
      .withHeaders(Header("Authorization", "Basic 123123123kjasdjkansdkja"))

    val res = route.loginRoute.run(req).value.unsafeRunSync()

    res match {
      case Some(value) => value.status shouldBe Status.Unauthorized
      case None        => fail()
    }
  }
}
