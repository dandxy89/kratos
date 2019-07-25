package com.dandxy.model.golf

sealed trait Par {
  def strokes: Int
}

object Par {

  case object ParThree extends Par {
    val strokes: Int = 3
  }

  case object ParFour extends Par {
    val strokes: Int = 4
  }

  case object ParFive extends Par {
    val strokes: Int = 5
  }
}
