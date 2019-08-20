package com.dandxy.golf.entity

import doobie.util.Meta

sealed trait Orientation {
  def description: String
  def code: Int
}

object Orientation {

  case object LongLeft extends Orientation {
    val description: String = "Long left"
    val code: Int           = 7
  }

  case object Long extends Orientation {
    val description: String = "Long"
    val code: Int           = 8
  }

  case object LongRight extends Orientation {
    val description: String = "Long right"
    val code: Int           = 9
  }

  case object MiddleLeft extends Orientation {
    val description: String = "Middle left"
    val code: Int           = 4
  }

  case object Middle extends Orientation {
    val description: String = "Middle"
    val code: Int           = 5
  }

  case object MiddleRight extends Orientation {
    val description: String = "Middle right"
    val code: Int           = 6
  }

  case object ShortLeft extends Orientation {
    val description: String = "Short left"
    val code: Int           = 1
  }

  case object Short extends Orientation {
    val description: String = "Short"
    val code: Int           = 2
  }

  case object ShortRight extends Orientation {
    val description: String = "Short right"
    val code: Int           = 3
  }

  def fromId(value: Int): Orientation = value match {
    case 1 => ShortLeft
    case 2 => Short
    case 3 => ShortRight
    case 4 => MiddleLeft
    case 5 => Middle
    case 6 => MiddleRight
    case 7 => LongLeft
    case 8 => Long
    case _ => LongRight
  }

  implicit val meta: Meta[Orientation] = Meta[Int].imap(fromId)(_.code)

}
