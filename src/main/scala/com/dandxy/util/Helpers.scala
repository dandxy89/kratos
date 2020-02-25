package com.dandxy.util

import cats.kernel.Monoid

object Helpers {

  def combineAll[A: Monoid](as: List[A]): A = as.foldLeft(Monoid[A].empty)(Monoid[A].combine)

  def roundAt3(n: Double): Double = {
    val s = math.pow(10, 3)
    math.round(n * s) / s
  }
}
