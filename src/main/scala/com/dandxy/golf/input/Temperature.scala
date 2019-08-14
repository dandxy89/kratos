package com.dandxy.golf.input

import doobie.util.Meta

sealed trait Temperature {
  def toCelsius: Double
}

object Temperature {

  final case class Celsius(value: Double) extends Temperature {
    def toCelsius: Double = value
  }

  final case class Fahrenheit(value: Double) extends Temperature {
    // (T(°F) - 32) / (9 / 5)
    def toCelsius: Double = (value - 32) / (9.0 / 5)
  }

  implicit val meta: Meta[Temperature] = Meta[Double].imap[Temperature](v => Celsius(v))(_.toCelsius)

}
