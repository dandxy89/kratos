package com.dandxy.model.golf

trait Penalty {
  def name: String
}

object Penalty {

  case object Hazard extends Penalty {
    val name: String = "Water hazards of all types"
  }

  case object OutOfBounds extends Penalty {
    val name: String = "Out of bounds"
  }

  case object LostBall extends Penalty {
    val name: String = "Lost ball"
  }

  case object UnplayableLie extends Penalty {
    val name: String = "Unplayable lie"
  }

  case object OneShotPenalty extends Penalty {
    val name: String = "Technical: One shot penalty"
  }

  case object TwoShotPenalty extends Penalty {
    val name: String = "Technical: Two shot penalty"
  }
}
