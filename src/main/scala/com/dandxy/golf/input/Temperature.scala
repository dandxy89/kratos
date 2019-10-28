package com.dandxy.golf.input

import io.circe.syntax._
import io.circe.{ Decoder, Encoder }

sealed trait Temperature {
  def toCelsius: Double
}

object Temperature {

  final case class Celsius(value: Double) extends Temperature {
    val toCelsius: Double = value
  }

  final case class Fahrenheit(value: Double) extends Temperature {
    def toCelsius: Double = (value - 32.0) / (9.0 / 5) // (T(°F) - 32) / (9 / 5)
  }

  implicit val en: Encoder[Temperature] = Encoder.instance(_.toCelsius.asJson)
  implicit val de: Decoder[Temperature] = Decoder.instance(_.as[Double].map(Celsius))
}
