package com.dandxy.model.golf.entity

import doobie.util.Meta

sealed trait Par {
  def strokes: Int
}

object Par {

  case object ParTwo extends Par {
    val strokes: Int = 2
  }

  case object ParThree extends Par {
    val strokes: Int = 3
  }

  case object ParFour extends Par {
    val strokes: Int = 4
  }

  case object ParFive extends Par {
    val strokes: Int = 5
  }

  case object ParSix extends Par {
    val strokes: Int = 6
  }

  case object ParSeven extends Par {
    val strokes: Int = 7
  }

  def fromInt(value: Int): Par = value match {
    case 3 => ParThree
    case 4 => ParFour
    case 5 => ParFive
    case 6 => ParSix
    case _ => ParSeven
  }

  implicit val meta: Meta[Par] = Meta[Int].imap(fromInt)(_.strokes)

}
