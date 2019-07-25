package com.dandxy.model.golf

sealed trait GolfClub {
  def displayName: String
  def dbIndex: Int
}

object GolfClub {

  case object Driver extends GolfClub {
    val displayName: String = "Driver"
    val dbIndex: Int        = 1
  }

  case object ThreeWood extends GolfClub {
    val displayName: String = "3 Wood"
    val dbIndex: Int        = 2
  }

  case object FourWood extends GolfClub {
    val displayName: String = "4 Wood"
    val dbIndex: Int        = 3
  }

  case object FiveWood extends GolfClub {
    val displayName: String = "5 Wood"
    val dbIndex: Int        = 4
  }

  case object SevenWood extends GolfClub {
    val displayName: String = "7 Wood"
    val dbIndex: Int        = 5
  }

  case object NineWood extends GolfClub {
    val displayName: String = "9 Wood"
    val dbIndex: Int        = 6
  }

  case object OneHybrid extends GolfClub {
    val displayName: String = "Hybrid 1"
    val dbIndex: Int        = 7
  }

  case object TwoHybrid extends GolfClub {
    val displayName: String = "Hybrid 2"
    val dbIndex: Int        = 8
  }

  case object ThreeHybrid extends GolfClub {
    val displayName: String = "Hybrid 3"
    val dbIndex: Int        = 9
  }

  case object FourHybrid extends GolfClub {
    val displayName: String = "Hybrid 4"
    val dbIndex: Int        = 10
  }

  case object FiveHybrid extends GolfClub {
    val displayName: String = "Hybrid 5"
    val dbIndex: Int        = 11
  }

  case object OneIron extends GolfClub {
    val displayName: String = "1 Iron"
    val dbIndex: Int        = 12
  }

  case object TwoIron extends GolfClub {
    val displayName: String = "2 Iron"
    val dbIndex: Int        = 13
  }

  case object ThreeIron extends GolfClub {
    val displayName: String = "3 Iron"
    val dbIndex: Int        = 14
  }

  case object FourIron extends GolfClub {
    val displayName: String = "4 Iron"
    val dbIndex: Int        = 15
  }

  case object FiveIron extends GolfClub {
    val displayName: String = "5 Iron"
    val dbIndex: Int        = 16
  }

  case object SixIron extends GolfClub {
    val displayName: String = "6 Iron"
    val dbIndex: Int        = 17
  }

  case object SevenIron extends GolfClub {
    val displayName: String = "7 Iron"
    val dbIndex: Int        = 18
  }

  case object EightIron extends GolfClub {
    val displayName: String = "8 Iron"
    val dbIndex: Int        = 19
  }

  case object NineIron extends GolfClub {
    val displayName: String = "9 Iron"
    val dbIndex: Int        = 20
  }

  case object PitchingWedge extends GolfClub {
    val displayName: String = "Pitching Wedge"
    val dbIndex: Int        = 21
  }

  case object GapWedge extends GolfClub {
    val displayName: String = "Gap Wedge"
    val dbIndex: Int        = 22
  }

  case object SandWedge extends GolfClub {
    val displayName: String = "Sand Wedge"
    val dbIndex: Int        = 23
  }

  case object LobWedge extends GolfClub {
    val displayName: String = "Lob Wedge"
    val dbIndex: Int        = 24
  }

  case object Putter extends GolfClub {
    val displayName: String = "Putter"
    val dbIndex: Int        = 25
  }

  def fromIndex(value: Int): GolfClub =
    value match {
      case 1  => Driver
      case 2  => ThreeWood
      case 3  => FourWood
      case 4  => FiveWood
      case 5  => SevenWood
      case 6  => NineWood
      case 7  => OneHybrid
      case 8  => TwoHybrid
      case 9  => ThreeHybrid
      case 10 => FourHybrid
      case 11 => FiveHybrid
      case 12 => OneIron
      case 13 => TwoIron
      case 14 => ThreeIron
      case 15 => FourIron
      case 16 => FiveIron
      case 17 => SixIron
      case 18 => SevenIron
      case 19 => EightIron
      case 20 => NineIron
      case 21 => PitchingWedge
      case 22 => GapWedge
      case 23 => SandWedge
      case 24 => LobWedge
      case _  => Putter
    }
}
