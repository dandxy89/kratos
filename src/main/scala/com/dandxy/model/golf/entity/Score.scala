package com.dandxy.model.golf.entity

import doobie.util.Meta

sealed trait Score {
  def name: String
  def s: Int
}

object Score {

  case object HoleInOne extends Score {
    val name: String = s"Ace"
    val s: Int       = -9
  }

  case object Albatross extends Score {
    val name: String = s"Albatross"
    val s: Int       = -3
  }

  case object Eagle extends Score {
    val name: String = s"Eagle"
    val s: Int       = -2
  }

  case object Birdie extends Score {
    val name: String = s"Birdie"
    val s: Int       = 1
  }

  case object ParredHole extends Score {
    val name: String = s"Par"
    val s: Int       = 0
  }

  case object Bogey extends Score {
    val name: String = s"Bogey"
    val s: Int       = 1
  }

  case object DoubleBogey extends Score {
    val name: String = s"Double Bogey"
    val s: Int       = 2
  }

  case object TripeBogey extends Score {
    val name: String = s"Double Bogey"
    val s: Int       = 3
  }

  final case class MultipleBogey(value: Int) extends Score {
    val name: String = s"${value}x Bogey: $value"
    val s: Int       = value
  }

  private def fromInt(shotCount: Int): Score = shotCount match {
    case -3 => Albatross
    case -2 => Eagle
    case -1 => Birdie
    case 0  => ParredHole
    case 1  => Bogey
    case 2  => DoubleBogey
    case 3  => TripeBogey
    case _  => MultipleBogey(shotCount)
  }

  def findScore(shotCount: Int, par: Par): Score =
    if (shotCount == 1) HoleInOne else fromInt(shotCount - par.strokes)

  def fromId(value: Int): Score = value match {
    case -9    => HoleInOne
    case -3    => Albatross
    case -2    => Eagle
    case -1    => Birdie
    case 0     => ParredHole
    case 1     => Bogey
    case 2     => DoubleBogey
    case 3     => TripeBogey
    case a @ _ => MultipleBogey(a)
  }

  implicit val meta: Meta[Score] = Meta[Int].imap(fromId)(_.s)

}
