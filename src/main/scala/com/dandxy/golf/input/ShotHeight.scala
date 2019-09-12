package com.dandxy.golf.input

import doobie.util.Meta
import io.circe.{ Decoder, Encoder }
import io.circe.syntax._

sealed trait ShotHeight {
  def description: String
  def id: Int
}

object ShotHeight {

  case object Low extends ShotHeight {
    val description: String = "Low"
    val id: Int             = 1
  }

  case object Normal extends ShotHeight {
    val description: String = "Medium"
    val id: Int             = 2
  }

  case object High extends ShotHeight {
    val description: String = "High"
    val id: Int             = 3
  }

  case object Putt extends ShotHeight {
    val description: String = "Putting"
    val id: Int             = 4
  }

  def fromId(value: Int): ShotHeight = value match {
    case 1 => Low
    case 2 => Normal
    case 3 => High
    case _ => Putt
  }

  // Instances
  implicit val meta: Meta[ShotHeight]  = Meta[Int].imap(fromId)(_.id)
  implicit val en: Encoder[ShotHeight] = Encoder.instance(_.id.asJson)
  implicit val de: Decoder[ShotHeight] = Decoder.instance(_.as[Int].map(fromId))
}
