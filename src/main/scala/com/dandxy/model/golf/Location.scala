package com.dandxy.model.golf

import cats.Eq
import doobie.implicits._
import doobie.util.fragment.Fragment

sealed trait Location {
  def name: String
}

sealed trait PGAStatistics {
  def dbLocation: Fragment
}

object Location {

  case object TeeBox extends Location with PGAStatistics {
    val name: String = "Tee"
    val dbLocation: Fragment = fr"pga.teelookup"
  }

  case object Fairway extends Location with PGAStatistics{
    val name: String = "Fairway"
    val dbLocation: Fragment = fr"pga.fairwaylookup"
  }

  case object Rough extends Location with PGAStatistics {
    val name: String = "Rough"
    val dbLocation: Fragment = fr"pga.roughlookup"
  }

  case object Bunker extends Location with PGAStatistics {
    val name: String = "Bunker"
    val dbLocation: Fragment = fr"pga.sandlookup"
  }

  case object Recovery extends Location with PGAStatistics {
    val name: String = "Recovery shot"
    val dbLocation: Fragment = fr"pga.recoverylookup"
  }

  case object OnTheGreen extends Location with PGAStatistics {
    val name: String = "Green"
    val dbLocation: Fragment = fr"pga.greenlookup"
  }

  case object InTheHole extends Location {
    val name: String = "Holed out"
  }

  final case class PenaltyShot(penalty: Penalty) extends Location {
    val name: String = "Penalty shot"
  }

  case object RedFlag extends Location {
    val name: String = "Red hazard"
  }

  case object MissedGreen extends Location {
    val name: String = "Missed green"
  }

  case object PenaltyArea extends Location {
    val name: String = "Penalty area"
  }

  case object Unplayable extends Location {
    val name: String = "Unplayable"
  }

  case object OutOfBounds extends Location {
    val name: String = "Out of bounds"
  }

  implicit val locationEq: Eq[Location] = Eq.instance { (a, b) =>
    a.name == b.name
  }
}
