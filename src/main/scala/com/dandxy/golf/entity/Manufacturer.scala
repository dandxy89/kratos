package com.dandxy.golf.entity

import doobie.util.Meta
import io.circe.{ Decoder, Encoder }
import io.circe.syntax._

trait Manufacturer {
  def company: String
  def id: Int
}

object Manufacturer {

  case object Miura extends Manufacturer {
    val company: String = "Miura"
    val id: Int         = 1
  }

  case object Titliest extends Manufacturer {
    val company: String = "Titliest"
    val id: Int         = 2
  }

  case object ScottyCameron extends Manufacturer {
    val company: String = "Scotty Cameron"
    val id: Int         = 3
  }

  case object Callaway extends Manufacturer {
    val company: String = "Callaway"
    val id: Int         = 4
  }

  case object Ping extends Manufacturer {
    val company: String = "Ping"
    val id: Int         = 5
  }

  def fromInt(value: Int): Manufacturer = value match {
    case 1 => Miura
    case 2 => Titliest
    case 3 => ScottyCameron
    case 4 => Callaway
    case _ => Ping
  }

  implicit val meta: Meta[Manufacturer]  = Meta[Int].imap(fromInt)(_.id)
  implicit val en: Encoder[Manufacturer] = Encoder.instance(_.id.asJson)
  implicit val de: Decoder[Manufacturer] = Decoder.instance(_.as[Int].map(fromInt))
}
