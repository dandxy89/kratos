package com.dandxy.strokes

import com.dandxy.model.golf.entity.Par
import com.dandxy.model.golf.input.{ Handicap, Points }

object StablefordCalculator {

  private[this] def round(value: Double): Int = Math.round(value.floatValue)

  def adjustedHandicap(handicap: Handicap): (Int, Int) =
    (round(handicap.value / 18.0), Math.floorMod(handicap.value.toLong, 18.toLong).toInt)

  def numberOfShots(handicap: Handicap, index: Int): Int =
    adjustedHandicap(handicap) match {
      case (_, b) if handicap.value < 0 => if (18 - b >= index) -1 else 0
      case (a, b) if b <= index         => a + (if (index <= b) 1 else 0)
      case (a, b)                       => a + (if (index <= b) 1 else 0)
    }

  def calculate(par: Par, handicap: Handicap, index: Int, shots: Int): Points =
    Points((shots - (par.strokes + numberOfShots(handicap, index))) match {
      case 1  => 1
      case 0  => 2
      case -1 => 3
      case -2 => 4
      case -3 => 5
      case -4 => 6
      case -5 => 7
      case _  => 0
    })
}
