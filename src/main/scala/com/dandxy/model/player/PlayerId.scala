package com.dandxy.model.player

import doobie.util.Meta

final case class PlayerId(id: Int)

object PlayerId {
  implicit val meta: Meta[PlayerId] = Meta[Int].imap(PlayerId(_))(_.id)
}
