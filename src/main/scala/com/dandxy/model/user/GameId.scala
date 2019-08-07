package com.dandxy.model.user

import doobie.util.Meta

final case class GameId(id: Double) extends AnyVal

object GameId {

  implicit val meta: Meta[GameId] = Meta[Double].imap(GameId(_))(_.id)

}
