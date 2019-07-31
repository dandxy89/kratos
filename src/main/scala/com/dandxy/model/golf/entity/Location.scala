package com.dandxy.model.golf.entity

import cats.Eq
import doobie.implicits._
import doobie.util.fragment.Fragment

sealed trait Location {
  def name: String
  def locationId: Int
}

sealed trait PGAStatistics {
  def dbLocation: Fragment
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
}
