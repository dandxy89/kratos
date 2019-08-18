package com.dandxy.db.util

import cats.Applicative
import com.dandxy.db.util.HealthCheck.Status
import io.circe.syntax._
import io.circe.{ Encoder, Json }
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import scala.language.higherKinds

final case class DatabaseStatus(postgres: Status)

object DatabaseStatus {

  implicit val encoder: Encoder[DatabaseStatus] = Encoder.instance { a =>
    Json.obj("postgres" -> Json.obj("status" -> a.postgres.entryName.asJson))
  }

  implicit def decoder[F[_]: Applicative]: EntityEncoder[F, DatabaseStatus] = jsonEncoderOf[F, DatabaseStatus]

}
