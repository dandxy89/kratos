package com.dandxy.model.golf.input

trait ShotHeight {
  def description: String
}

object ShotHeight {

  case object Low extends ShotHeight {
    val description: String = "Low"
  }

  case object Normal extends ShotHeight {
    val description: String = "Medium"
  }

  case object High extends ShotHeight {
    val description: String = "High"
  }
}
