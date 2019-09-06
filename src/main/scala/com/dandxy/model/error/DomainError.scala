package com.dandxy.model.error

import cats.Applicative
import com.dandxy.golf.input.Distance
import io.circe.syntax._
import io.circe.{ Encoder, Json }
import org.http4s.circe.jsonEncoderOf
import org.http4s.headers.`Content-Type`
import org.http4s.{ EntityEncoder, MediaType }

import scala.language.higherKinds

sealed trait DomainError {
  def msg: String
}

object DomainError {

  final case class InvalidDistance(distance: Distance) extends DomainError {
    def msg: String = s" Invalid distance: $distance"
  }

  final case class StatisticNotKnown(distance: Distance) extends DomainError {
    def msg: String = s" Statistic distance not known: $distance"
  }

  case object InvalidDataProvided extends DomainError {
    override def msg: String = "Invalid data provide to service"
  }

  implicit val e: Encoder[DomainError] = Encoder.instance { e =>
    Json.obj("error" -> e.msg.asJson)
  }

  implicit def ee[F[_]: Applicative]: EntityEncoder[F, DomainError] =
    jsonEncoderOf[F, DomainError]
      .withContentType(`Content-Type`(MediaType.application.json))
}
