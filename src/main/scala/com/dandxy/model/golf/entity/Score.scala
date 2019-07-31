package com.dandxy.model.golf.entity

sealed trait Score {
  def name: String
}

object Score {

  final case class HoleInOne(value: Int) extends Score {
    val name: String = s"Ace: $value"
  }

  final case class Albatross(value: Int) extends Score {
    val name: String = s"Albatross: $value"
  }

  final case class Eagle(value: Int) extends Score {
    val name: String = s"Eagle: $value"
  }

  final case class Birdie(value: Int) extends Score {
    val name: String = s"Birdie: $value"
  }

  final case class ParredHole(value: Int) extends Score {
    val name: String = s"Par: $value"
  }

  final case class Bogey(value: Int) extends Score {
    val name: String = s"Bogey: $value"
  }

  final case class DoubleBogey(value: Int) extends Score {
    val name: String = s"Double Bogey: $value"
  }

  final case class TripeBogey(value: Int) extends Score {
    val name: String = s"Double Bogey: $value"
  }

  final case class MultipleBogey(value: Int) extends Score {
    val name: String = s"${value}x Bogey: $value"
  }

  def findScore(shotCount: Int, par: Par): Score =
    if (shotCount == 1) HoleInOne(1)
    else {
      shotCount - par.strokes match {
        case -3 => Albatross(shotCount)
        case -2 => Eagle(shotCount)
        case -1 => Birdie(shotCount)
        case 0  => ParredHole(shotCount)
        case 1  => Bogey(shotCount)
        case 2  => DoubleBogey(shotCount)
        case 3  => TripeBogey(shotCount)
        case _  => MultipleBogey(shotCount)
      }
    }
}
