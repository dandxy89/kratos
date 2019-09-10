package com.dandxy.model.user

import doobie.util.Meta
import io.circe.Encoder
import io.circe.syntax._

sealed trait Identifier {
  def id: Int
}

object Identifier {

  final case class GameId(id: Int) extends Identifier

  object GameId {
    // Instances
    implicit val meta: Meta[GameId]  = Meta[Int].imap(GameId(_))(_.id)
    implicit val en: Encoder[GameId] = Encoder.instance(_.id.asJson)
  }

  final case class Hole(id: Int) extends Identifier

  object Hole {
    // Instances
    implicit val meta: Meta[Hole]  = Meta[Int].imap(Hole(_))(_.id)
    implicit val en: Encoder[Hole] = Encoder.instance(_.id.asJson)
  }
}
