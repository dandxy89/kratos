package com.dandxy.middleware

import cats.effect.IO
import com.dandxy.middleware.OutputType.{ ACase, CustomModelA, CustomModelB }
import com.dandxy.middleware.http4s.ToHttpResponse
import com.dandxy.middleware.http4s.content.defaults._
import com.dandxy.middleware.http4s.content.syntax._
import io.circe.Encoder
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.{ EntityEncoder, Header, Method, Request, Response, Status }
import org.scalatest.{ FlatSpec, Matchers }

trait OutputType

object OutputType {
  final case class ACase(b: Int)
  final case class CustomModelA(a: Int, b: String) extends OutputType
  final case class CustomModelB(a: Int, b: String) extends OutputType

  val testPartial: PartialFunction[OutputType, Status] = {
    case _: CustomModelA => Status.EarlyHints
    case _: CustomModelB => Status.Continue
  }

  implicit val encoder: Encoder[OutputType] = Encoder.instance {
    case CustomModelA(_, b) => b.asJson
    case CustomModelB(_, _) => "THIS SHOULD WORK".asJson
  }

  implicit val entityEncoder: EntityEncoder[IO, OutputType] = jsonEncoderOf[IO, OutputType]
}

class ToResponseTest extends FlatSpec with Matchers {

  implicit val entityEncoder: EntityEncoder[IO, ACase] = EntityEncoder.emptyEncoder

  it should "compile an auto ToResponse" in {
    val res: Response[IO] = IO
      .pure[Either[String, ACase]](Right(ACase(2)))
      .negotiate[IO](Request[IO](Method.GET).withHeaders(Header("Accept", "application/json")))
      .unsafeRunSync()

    res.status shouldBe Status.Ok
  }

  implicit val enc: ToHttpResponse[IO, OutputType] =
    http4s.content.fromEncoder(OutputType.testPartial, _ => true)

  it should "Writing a custom fromEncoder" in {
    val resA: Response[IO] = IO
      .pure[Either[String, OutputType]](Right(CustomModelA(1, "DanD")))
      .negotiate[IO](Request[IO](Method.GET).withHeaders(Header("Accept", "application/json")))
      .unsafeRunSync()

    val resB: Response[IO] = IO
      .pure[Either[String, OutputType]](Right(CustomModelB(1, "Dan")))
      .negotiate[IO](Request[IO](Method.GET).withHeaders(Header("Accept", "application/json")))
      .unsafeRunSync()

    resA.as[String].unsafeRunSync() shouldBe """"DanD""""
    resB.as[String].unsafeRunSync() shouldBe """"THIS SHOULD WORK""""

  }

  it should "return Not Acceptable" in {
    // Missing header should still return not acceptable
    val resC: Response[IO] = IO
      .pure[Either[String, OutputType]](Right(CustomModelB(1, "Dan")))
      .negotiate[IO](Request[IO](Method.GET))
      .unsafeRunSync()

    resC.status shouldBe Status.NotAcceptable
  }
}
