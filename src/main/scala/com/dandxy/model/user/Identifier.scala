package com.dandxy.model.user

import io.circe.syntax._
import io.circe.{Decoder, Encoder}

sealed trait Identifier {
  def id: Int
}

object Identifier {

  final case class GameId(id: Int) extends Identifier

  object GameId {
    implicit val en: Encoder[GameId] = Encoder.instance(_.id.asJson)
    implicit val de: Decoder[GameId] = Decoder.instance(_.as[Int].map(GameId(_)))
  }

  final case class Hole(id: Int) extends Identifier

  object Hole {
    implicit val en: Encoder[Hole] = Encoder.instance(_.id.asJson)
    implicit val de: Decoder[Hole] = Decoder.instance(_.as[Int].map(Hole(_)))
  }
}
