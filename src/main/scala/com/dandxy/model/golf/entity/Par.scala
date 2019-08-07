package com.dandxy.model.golf.entity

import doobie.util.Meta

sealed trait Par {
  def strokes: Int
}

object Par {

  case object ParThree extends Par {
    val strokes: Int = 3
  }

  case object ParFour extends Par {
    val strokes: Int = 4
  }

  case object ParFive extends Par {
    val strokes: Int = 5
  }

  def fromInt(value: Int): Par = value match {
    case 3 => ParThree
    case 4 => ParFour
    case _ => ParFive
  }

  implicit val meta: Meta[Par] = Meta[Int].imap(fromInt)(_.strokes)

}
