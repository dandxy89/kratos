package com.dandxy.jwt

import java.security.KeyPairGenerator

import cats.effect.IO
import javax.crypto.{ KeyGenerator, SecretKey }
import org.http4s._
import org.http4s.headers.Authorization
import org.http4s.server.AuthMiddleware
import org.scalatest.Matchers
import pdi.jwt.{ Jwt, JwtAlgorithm, JwtClaim }

class JwtAuthMiddlewareSpec extends Http4sSpec with Matchers {

  val secretKey: String                      = "secret-key"
  val middleware: AuthMiddleware[IO, Claims] = JwtAuthMiddleware[IO, Claims](secretKey, Seq(JwtAlgorithm.HS512))

  "JwtAuthMiddleware" should "return 403 Forbidden when auth header is not present" in {
    val req      = Request[IO](Method.GET, Uri.unsafeFromString("/some-endpoint"))
    val response = handleRequest(middleware, req).unsafeRunSync()
    response.status shouldBe Status.Forbidden
  }

  it should "return 403 Forbidden when token is not valid and URL is not found" in {
    val token   = Jwt.encode(JwtClaim(content = """{"playerId": 1}"""), "some-other-secret", JwtAlgorithm.HS512)
    val headers = Headers.of(Authorization(Credentials.Token(AuthScheme.Bearer, token)))
    val req     = Request[IO](Method.GET, Uri.unsafeFromString("/nonexistent"), headers = headers)

    val response = handleRequest(middleware, req).unsafeRunSync()
    response.status shouldBe Status.Forbidden
  }

  it should "return 403 Forbidden when the auth header is not using the bearer token scheme and URL is found" in {
    val headers: Headers = Headers.of(Authorization(BasicCredentials("some-username", "some-password")))
    val req              = Request[IO](Method.GET, Uri.unsafeFromString("/some-endpoint"), headers = headers)

    val response = handleRequest(middleware, req).unsafeRunSync()
    response.status shouldBe Status.Forbidden
  }

  it should "return 403 Forbidden when JWT token is not valid and URL is found" in {
    val token = Jwt.encode(
      JwtClaim(content = "some-content"),
      "wrong-secret",
      JwtAlgorithm.HS512
    )
    val headers: Headers = Headers.of(Authorization(Credentials.Token(AuthScheme.Bearer, token)))
    val req              = Request[IO](Method.GET, Uri.unsafeFromString("/some-endpoint"), headers = headers)

    val response = handleRequest(middleware, req).unsafeRunSync()
    response.status shouldBe Status.Forbidden
  }

  it should "return 403 Forbidden when JWT algorithm is not matching and URL is found" in {
    val token = Jwt.encode(
      JwtClaim(content = """{"user-id": "some-user-id", "username": "some-user-name"}"""),
      "wrong-secret",
      JwtAlgorithm.HS256
    )
    val headers: Headers = Headers.of(Authorization(Credentials.Token(AuthScheme.Bearer, token)))
    val req              = Request[IO](Method.GET, Uri.unsafeFromString("/some-endpoint"), headers = headers)

    val response = handleRequest(middleware, req).unsafeRunSync()
    response.status shouldBe Status.Forbidden
  }

  it should "return 200 when token is valid and URL is found" in {
    val token   = Jwt.encode(JwtClaim(content = """{"playerId": 1}"""), secretKey, JwtAlgorithm.HS512)
    val headers = Headers.of(Authorization(Credentials.Token(AuthScheme.Bearer, token)))
    val req     = Request[IO](Method.GET, Uri.unsafeFromString("/some-endpoint"), headers = headers)

    val response = handleRequest(middleware, req).unsafeRunSync()
    response.status shouldBe Status.Ok
  }

  it should "return 404 when token is valid but URL is not found" in {
    val token   = Jwt.encode(JwtClaim(content = """{"playerId": 1}"""), secretKey, JwtAlgorithm.HS512)
    val headers = Headers.of(Authorization(Credentials.Token(AuthScheme.Bearer, token)))
    val req     = Request[IO](Method.GET, Uri.unsafeFromString("/nonexistent"), headers = headers)

    val response = handleRequest(middleware, req).unsafeRunSync()
    response.status shouldBe Status.NotFound
  }

  it should "return 200 OK when a javax.crypto.SecretKey is provided and token is valid" in {
    val secretKey: SecretKey = KeyGenerator.getInstance("AES").generateKey()

    val token      = Jwt.encode(JwtClaim(content = """{"playerId": 1}"""), secretKey, JwtAlgorithm.HS512)
    val headers    = Headers.of(Authorization(Credentials.Token(AuthScheme.Bearer, token)))
    val req        = Request[IO](Method.GET, Uri.unsafeFromString("/some-endpoint"), headers = headers)
    val middleware = JwtAuthMiddleware[IO, Claims](secretKey, Seq(JwtAlgorithm.HS512))

    val response = handleRequest(middleware, req).unsafeRunSync()
    response.status shouldBe Status.Ok
  }

  it should "return 200 OK when a java.security.PrivateKey is provided and token is valid" in {
    val keyPair = KeyPairGenerator.getInstance("RSA").genKeyPair()

    val token      = Jwt.encode(JwtClaim(content = """{"playerId": 1}"""), keyPair.getPrivate, JwtAlgorithm.RS512)
    val headers    = Headers.of(Authorization(Credentials.Token(AuthScheme.Bearer, token)))
    val req        = Request[IO](Method.GET, Uri.unsafeFromString("/some-endpoint"), headers = headers)
    val middleware = JwtAuthMiddleware[IO, Claims](keyPair.getPublic, JwtAlgorithm.allRSA())

    val response = handleRequest(middleware, req).unsafeRunSync()
    response.status shouldBe Status.Ok
  }
}
