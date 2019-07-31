package com.dandxy.model.golf.entity

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
    val name: String         = "Tee"
    val dbLocation: Fragment = fr"pga.teelookup"
  }

  case object Fairway extends Location with PGAStatistics {
    val name: String         = "Fairway"
    val dbLocation: Fragment = fr"pga.fairwaylookup"
  }

  case object Rough extends Location with PGAStatistics {
    val name: String         = "Rough"
    val dbLocation: Fragment = fr"pga.roughlookup"
  }

  case object Bunker extends Location with PGAStatistics {
    val name: String         = "Bunker"
    val dbLocation: Fragment = fr"pga.sandlookup"
  }

  case object Recovery extends Location with PGAStatistics {
    val name: String         = "Recovery shot"
    val dbLocation: Fragment = fr"pga.recoverylookup"
  }

  case object OnTheGreen extends Location with PGAStatistics {
    val name: String         = "Green"
    val dbLocation: Fragment = fr"pga.greenlookup"
  }

  val approachLies: Set[Location] = Set(Bunker, Rough, Fairway, Recovery, TeeBox)

  implicit val locationEq: Eq[Location] = Eq.instance { (a, b) =>
    a.name == b.name
  }
}
