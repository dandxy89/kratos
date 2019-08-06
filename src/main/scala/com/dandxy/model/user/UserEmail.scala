package com.dandxy.model.user

import doobie.util.Meta

final case class UserEmail(email: String) extends AnyVal

object UserEmail {

  implicit val meta: Meta[UserEmail] = Meta[String].imap(UserEmail(_))(_.email)

}
