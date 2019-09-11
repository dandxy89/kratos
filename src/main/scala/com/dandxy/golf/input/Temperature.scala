package com.dandxy.golf.input

import doobie.util.Meta
import io.circe.{ Decoder, Encoder }
import io.circe.syntax._

sealed trait Temperature {
  def toCelsius: Double
}

object Temperature {

  final case class Celsius(value: Double) extends Temperature {
    val toCelsius: Double = value
  }

  final case class Fahrenheit(value: Double) extends Temperature {
    // (T(°F) - 32) / (9 / 5)
    def toCelsius: Double = (value - 32.0) / (9.0 / 5)
  }

  // Instances
  implicit val meta: Meta[Temperature]  = Meta[Double].imap[Temperature](v => Celsius(v))(_.toCelsius)
  implicit val en: Encoder[Temperature] = Encoder.instance(_.toCelsius.asJson)
  implicit val de: Decoder[Temperature] = Decoder.instance(_.as[Double].map(Celsius))

}
