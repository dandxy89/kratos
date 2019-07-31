package com.dandxy.model.player

sealed trait PlayingStyle {
  def description: String
}

object PlayingStyle {

  case object RightHanded extends PlayingStyle {
    val description: String = "Right-handed"
  }

  case object LeftHanded extends PlayingStyle {
    val description: String = "Left-handed"
  }
}
