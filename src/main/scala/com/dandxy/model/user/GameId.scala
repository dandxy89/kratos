package com.dandxy.model.user

import doobie.util.Meta

final case class GameId(id: Int) extends AnyVal

object GameId {

  implicit val meta: Meta[GameId] = Meta[Int].imap(GameId(_))(_.id)

}
