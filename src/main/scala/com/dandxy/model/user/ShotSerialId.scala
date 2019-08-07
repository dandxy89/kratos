package com.dandxy.model.user

import doobie.util.Meta

final case class ShotSerialId(id: Int) extends AnyVal

object ShotSerialId {

  implicit val meta: Meta[ShotSerialId] = Meta[Int].imap(ShotSerialId(_))(_.id)

}
