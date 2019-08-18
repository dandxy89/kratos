package com.dandxy.middleware

import cats.Monad
import cats.effect.{IO, Sync}
import com.dandxy.middleware.TestError.{ErrorA, ErrorB}
import com.dandxy.middleware.http4s.ToHttpResponse
import com.dandxy.middleware.http4s.syntax._
import com.dandxy.middleware.http4s.defaults._
import io.circe.syntax._
import io.circe.{Encoder, Json}
import org.http4s.circe._
import org.http4s.{Header, MediaRange, MediaType, Method, Request, Response, Status, Uri}
import org.scalatest.{FlatSpec, Matchers}

import scala.language.higherKinds

trait TestError {
  def message: String
  def detailedMessage: String
}

object TestError {

  case object ErrorA extends TestError {
    override def message: String         = "Error A found"
    override def detailedMessage: String = "Error A detailed"
  }

  case object ErrorB extends TestError {
    override def message: String         = "Error B found"
    override def detailedMessage: String = "Error B detailed"
  }

  implicit val e: Encoder[TestError] = Encoder.instance {
    case e @ ErrorA => Json.obj("error" -> e.message.asJson)
    case e @ ErrorB => Json.obj("error" -> e.detailedMessage.asJson)
  }
}

class MatchMediaResponseTest extends FlatSpec with Matchers {

  def testErrorMediaMatcher[F[_]](implicit F: Monad[F]): TestError => PartialFunction[MediaRange, F[Response[F]]] = err => {
    case MediaType.application.json | MediaType.application.xml =>
      F.pure(Response[F](Status.Ok).withEntity(err.asJson))
  }

  implicit def toResponse[F[_]: Sync]: ToHttpResponse[F, TestError] =
    ToResponse.matching[F, List[MediaRange], MediaRange, Response[F], TestError](testErrorMediaMatcher)

  it should "the media matcher should respond correctly when a valid Accept header has been passed" in {

    def generateResponse[F[_]](acceptHeader: String, value: TestError): Response[IO] = {
      val uri = Uri.unsafeFromString("/someRoute")
      val req = Request[IO](Method.GET, uri).withHeaders(Header("Accept", acceptHeader))

      IO.pure[Either[TestError, String]](Left(value)).negotiate[IO](req).unsafeRunSync()
    }

    generateResponse("application/json", ErrorA).as[String].unsafeRunSync() shouldBe """{"error":"Error A found"}"""
    generateResponse("application/xml", ErrorB).as[String].unsafeRunSync() shouldBe """{"error":"Error B detailed"}"""
    generateResponse("application/gzip", ErrorB).status shouldBe Status.NotAcceptable
  }
}
