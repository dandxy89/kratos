package com.dandxy.golf.input

import org.scalatest.{FlatSpec, Matchers}

class TemperatureSpec extends FlatSpec with Matchers {

  behavior of "Temperature"

  Temperature.Celsius(24.0).toCelsius shouldBe 24.0
  Temperature.Celsius(36.9).toCelsius shouldBe 36.9

  Temperature.Fahrenheit(36.9).toCelsius shouldBe 2.7222222222222214
  Temperature.Fahrenheit(102.9).toCelsius shouldBe 39.38888888888889
}
