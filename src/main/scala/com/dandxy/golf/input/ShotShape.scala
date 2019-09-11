package com.dandxy.golf.input

import doobie.util.Meta
import io.circe.{ Decoder, Encoder }
import io.circe.syntax._

sealed trait ShotShape {
  def description: String
  def id: Int
}

object ShotShape {

  case object Straight extends ShotShape {
    val description: String = "Straight"
    val id: Int             = 1
  }

  case object Fade extends ShotShape {
    val description: String = "Fade"
    val id: Int             = 2
  }

  case object Draw extends ShotShape {
    val description: String = "Draw"
    val id: Int             = 3
  }

  case object PushFade extends ShotShape {
    val description: String = "PushFade"
    val id: Int             = 4
  }

  case object PushDraw extends ShotShape {
    val description: String = "PushDraw"
    val id: Int             = 5
  }

  case object Slice extends ShotShape {
    val description: String = "Slice"
    val id: Int             = 6
  }

  case object Hook extends ShotShape {
    val description: String = "Hook"
    val id: Int             = 7
  }

  case object Topped extends ShotShape {
    val description: String = "Topped"
    val id: Int             = 8
  }

  case object RoofedIt extends ShotShape {
    val description: String = "RoofedIt"
    val id: Int             = 9
  }

  case object Stinger extends ShotShape {
    val description: String = "Stinger"
    val id: Int             = 10
  }

  def fromId(value: Int): ShotShape = value match {
    case 1 => Straight
    case 2 => Fade
    case 3 => Draw
    case 4 => PushFade
    case 5 => PushDraw
    case 6 => Slice
    case 7 => Hook
    case 8 => Topped
    case 9 => RoofedIt
    case _ => Stinger
  }

  // Instances
  implicit val meta: Meta[ShotShape]  = Meta[Int].imap(fromId)(_.id)
  implicit val en: Encoder[ShotShape] = Encoder.instance(_.id.asJson)
  implicit val de: Decoder[ShotShape] = Decoder.instance(_.as[Int].map(fromId))
}
