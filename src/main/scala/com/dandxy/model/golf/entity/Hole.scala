package com.dandxy.model.golf.entity

import doobie.util.Meta

final case class Hole(number: Int) extends AnyVal

object Hole {

  implicit val meta: Meta[Hole] = Meta[Int].imap(Hole(_))(_.number)

}
