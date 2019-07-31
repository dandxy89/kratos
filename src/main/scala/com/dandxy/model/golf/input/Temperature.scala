package com.dandxy.model.golf.input

sealed trait Temperature {
  def toCelsius: Int
}

object Temperature {

  final case class Celsius(value: Int) extends Temperature {
    def toCelsius: Int = value
  }

  final case class Fahrenheit(value: Int) extends Temperature {
    // (T(°F) - 32) / (9 / 5)
    def toCelsius: Int = ((value - 32) / (9.0 / 5)).toInt
  }
}
