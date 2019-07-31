package com.dandxy.model.golf.entity

sealed trait Orientation {
  def description: String
  def code: Int
}

object Orientation {

  case object LongLeft extends Orientation {
    val description: String = "Long left"
    val code: Int           = 7
  }

  case object Long extends Orientation {
    val description: String = "Long"
    val code: Int           = 8
  }

  case object LongRight extends Orientation {
    val description: String = "Long right"
    val code: Int           = 9
  }

  case object MiddleLeft extends Orientation {
    val description: String = "Middle lett"
    val code: Int           = 4
  }

  case object Middle extends Orientation {
    val description: String = "Middle"
    val code: Int           = 5
  }

  case object MiddleRight extends Orientation {
    val description: String = "Middle right"
    val code: Int           = 6
  }

  case object ShortLeft extends Orientation {
    val description: String = "Short left"
    val code: Int           = 1
  }

  case object Short extends Orientation {
    val description: String = "Short"
    val code: Int           = 2
  }

  case object ShortRight extends Orientation {
    val description: String = "Short right"
    val code: Int           = 3
  }
}
