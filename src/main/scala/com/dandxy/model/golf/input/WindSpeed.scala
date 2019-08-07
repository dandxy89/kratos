package com.dandxy.model.golf.input

import doobie.util.Meta

sealed trait WindSpeed {
  def description: String
  def id: Int
}

object WindSpeed {

  case object NoWind extends WindSpeed {
    val description: String = "No wind"
    val id: Int             = 1
  }

  case object LightWind extends WindSpeed {
    val description: String = "Light wind"
    val id: Int             = 2
  }

  case object MediumWindy extends WindSpeed {
    val description: String = "Medium strength wind"
    val id: Int             = 3
  }

  case object StrongWindy extends WindSpeed {
    val description: String = "Strong wind"
    val id: Int             = 4
  }

  def fromId(value: Int): WindSpeed = value match {
    case 1 => LightWind
    case 2 => NoWind
    case 3 => MediumWindy
    case _ => StrongWindy
  }

  implicit val meta: Meta[WindSpeed] = Meta[Int].imap(fromId)(_.id)

}
