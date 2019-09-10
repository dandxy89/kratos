package com.dandxy.jwt

import io.circe.generic.auto._
import io.circe.parser

final case class Claims(playerId: Int)

object Claims {
  implicit val jwtDecoder: JwtContentDecoder[Claims] =
    (claims: String) => parser.decode[Claims](claims).left.map(_.getMessage)
}
