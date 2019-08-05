package com.dandxy.model.golf.entity

import cats.Eq
import com.dandxy.model.golf.entity.Penalty._
import doobie.implicits._
import doobie.util.fragment.Fragment

sealed trait Location {
  def name: String
  def locationId: Int
}

sealed trait PGAStatistics {
  def dbLocation: Fragment
}

sealed trait Penalty extends Location {
  def description: String
  def shots: Int
}

object Penalty {

  case object Hazard extends Penalty {
    val description: String = "Water hazards of all types"
    val shots: Int          = 1

    val name: String    = "Penalty - hazard"
    val locationId: Int = 7
  }

  case object OutOfBounds extends Penalty {
    val description: String = "Out of bounds"
    val shots: Int          = 1

    val name: String    = "Penalty - out of bounds"
    val locationId: Int = 8
  }

  case object LostBall extends Penalty {
    val description: String = "Lost ball"
    val shots: Int          = 1

    val name: String    = "Penalty - lost ball"
    val locationId: Int = 9
  }

  case object UnplayableLie extends Penalty {
    val description: String = "Unplayable lie"
    val shots: Int          = 1

    val name: String    = "Penalty - unplayable lie"
    val locationId: Int = 10
  }

  case object OneShotPenalty extends Penalty {
    val description: String = "Technical: One shot penalty"
    val shots: Int          = 1

    val name: String    = "Penalty - one shot penalty"
    val locationId: Int = 11
  }

  case object TwoShotPenalty extends Penalty {
    val description: String = "Technical: Two shot penalty"
    val shots: Int          = 2

    val name: String    = "Penalty - two shot penalty"
    val locationId: Int = 12
  }
}

object Location {

  case object TeeBox extends Location with PGAStatistics {
    val name: String         = "Tee"
    val dbLocation: Fragment = fr"pga.teelookup"
    val locationId: Int      = 1
  }

  case object Fairway extends Location with PGAStatistics {
    val name: String         = "Fairway"
    val dbLocation: Fragment = fr"pga.fairwaylookup"
    val locationId: Int      = 2
  }

  case object Rough extends Location with PGAStatistics {
    val name: String         = "Rough"
    val dbLocation: Fragment = fr"pga.roughlookup"
    val locationId: Int      = 3
  }

  case object Bunker extends Location with PGAStatistics {
    val name: String         = "Bunker"
    val dbLocation: Fragment = fr"pga.sandlookup"
    val locationId: Int      = 4
  }

  case object Recovery extends Location with PGAStatistics {
    val name: String         = "Recovery shot"
    val dbLocation: Fragment = fr"pga.recoverylookup"
    val locationId: Int      = 5
  }

  case object OnTheGreen extends Location with PGAStatistics {
    val name: String         = "Green"
    val dbLocation: Fragment = fr"pga.greenlookup"
    val locationId: Int      = 6
  }

  val approachLies: Set[Location] = Set(Bunker, Rough, Fairway, Recovery, TeeBox)

  implicit val locationEq: Eq[Location] = Eq.instance { (a, b) =>
    a.name == b.name
  }

  def locationIdToLocation(value: Int): Location = value match {
    case 1  => TeeBox
    case 2  => Fairway
    case 3  => Rough
    case 4  => Bunker
    case 5  => Recovery
    case 6  => OnTheGreen
    case 7  => Hazard
    case 8  => OutOfBounds
    case 9  => LostBall
    case 10 => UnplayableLie
    case 11 => OneShotPenalty
    case _  => TwoShotPenalty
  }
}
