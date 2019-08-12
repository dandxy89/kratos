package com.dandxy.model.user

import doobie.util.Meta

final case class Password(value: String) extends AnyVal

object Password {
  implicit val meta: Meta[Password] = Meta[String].imap(Password(_))(_.value)
}
