package com.dandxy.model.user

import doobie.util.Meta

sealed trait Identifier {
  def id: Int
}

object Identifier {

  final case class GameId(id: Int) extends Identifier

  object GameId {
    implicit val meta: Meta[GameId] = Meta[Int].imap(GameId(_))(_.id)
  }

  final case class Hole(id: Int) extends Identifier

  object Hole {
    implicit val meta: Meta[Hole] = Meta[Int].imap(Hole(_))(_.id)
  }
}
