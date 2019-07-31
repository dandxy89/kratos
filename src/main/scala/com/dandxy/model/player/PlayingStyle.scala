package com.dandxy.model.player

sealed trait PlayingStyle {
  def description: String
  def id: Int
}

object PlayingStyle {

  case object RightHanded extends PlayingStyle {
    val description: String = "Right-handed"
    val id: Int             = 1
  }

  case object LeftHanded extends PlayingStyle {
    val description: String = "Left-handed"
    val id: Int             = 3
  }
}
