package com.dandxy.model.error

import com.dandxy.golf.input.Distance
import io.circe.syntax._
import io.circe.{ Encoder, Json }

sealed trait DomainError {
  def msg: String
}

object DomainError {

  final case class InvalidDistance(distance: Distance) extends DomainError {
    val msg: String = s" Invalid distance: $distance"
  }

  final case class StatisticNotKnown(distance: Distance) extends DomainError {
    val msg: String = s" Statistic distance not known: $distance"
  }

  case object InvalidDataProvided extends DomainError {
    val msg: String = "Invalid data provided to service"
  }

  case object InvalidPlayerProvided extends DomainError {
    val msg: String = "Invalid player id"
  }

  case object InvalidGameProvided extends DomainError {
    val msg: String = "Invalid game id"
  }

  case object NoContentInDB extends DomainError {
    val msg: String = ""
  }

  implicit val e: Encoder[DomainError] = Encoder.instance { e =>
    Json.obj("error" -> e.msg.asJson)
  }
}
