package com.dandxy.golf.input

sealed trait Metric {
  def description: String
  def group: String
}

object Metric {

  case object TeeToGreen extends Metric {
    val description: String = "Strokes gained tee to green"
    val group: String       = "Off The Tee"
  }

  case object OffTheTee extends Metric {
    val description: String = "Strokes gained off the tee"
    val group: String       = "Off The Tee"
  }

  case object ApproachTheGreen extends Metric {
    val description: String = "Strokes gained around the green"
    val group: String       = "Approach the Green"
  }

  case object AroundTheGreen extends Metric {
    val description: String = "Strokes gained around the green"
    val group: String       = "Approach the Green"
  }

  case object Putting extends Metric {
    val description: String = "Strokes gained putting"
    val group: String       = "Putting"
  }
}
