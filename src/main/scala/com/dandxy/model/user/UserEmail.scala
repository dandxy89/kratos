package com.dandxy.model.user

import doobie._

final case class UserEmail(email: String) extends AnyVal

object UserEmail {
  implicit val p: Put[UserEmail] = Put[String].tcontramap(_.email)
}
