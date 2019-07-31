package com.dandxy.model.golf.input

sealed trait WindSpeed {
  def description: String
}

object WindSpeed {

  case object NoWind extends WindSpeed {
    val description: String = "No wind"
  }

  case object LightWind extends WindSpeed {
    val description: String = "Light wind"
  }

  case object MediumWindy extends WindSpeed {
    val description: String = "Medium strength wind"
  }

  case object StrongWindy extends WindSpeed {
    val description: String = "Strong wind"
  }
}
