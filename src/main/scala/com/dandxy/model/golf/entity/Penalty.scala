package com.dandxy.model.golf.entity

trait Penalty {
  def name: String
  def shots: Int
}

object Penalty {

  case object Hazard extends Penalty {
    val name: String = "Water hazards of all types"
    val shots: Int   = 1
  }

  case object OutOfBounds extends Penalty {
    val name: String = "Out of bounds"
    val shots: Int   = 1
  }

  case object LostBall extends Penalty {
    val name: String = "Lost ball"
    val shots: Int   = 1
  }

  case object UnplayableLie extends Penalty {
    val name: String = "Unplayable lie"
    val shots: Int   = 1
  }

  case object OneShotPenalty extends Penalty {
    val name: String = "Technical: One shot penalty"
    val shots: Int   = 1
  }

  case object TwoShotPenalty extends Penalty {
    val name: String = "Technical: Two shot penalty"
    val shots: Int   = 2
  }
}
