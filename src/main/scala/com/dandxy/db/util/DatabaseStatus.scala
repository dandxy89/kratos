package com.dandxy.db.util

import com.dandxy.db.util.HealthCheck.Status
import io.circe.syntax._
import io.circe.{ Encoder, Json }

final case class DatabaseStatus(postgres: Status)

object DatabaseStatus {

  implicit val encoder: Encoder[DatabaseStatus] = Encoder.instance { a =>
    Json.obj("postgres" -> Json.obj("status" -> a.postgres.entryName.asJson))
  }
}
