package com.dandxy.model.user

import doobie.util.Meta

final case class PlayerId(uuid: Int) extends AnyVal

object PlayerId {

  implicit val meta: Meta[PlayerId] = Meta[Int].imap(PlayerId(_))(_.uuid)

}
