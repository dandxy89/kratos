package com.dandxy.golf.input

import com.dandxy.golf.input.DistanceMeasurement.{ Feet, Meters, Yards }
import org.scalatest.{ FlatSpec, Matchers }

class DistanceMeasurementSpec extends FlatSpec with Matchers {

  behavior of "DistanceMeasurementSpec"

  it should "fromId" in {
    DistanceMeasurement.fromId(1) shouldBe Yards
    DistanceMeasurement.fromId(2) shouldBe Meters
    DistanceMeasurement.fromId(3) shouldBe Feet
    DistanceMeasurement.fromId(4) shouldBe Feet
  }
}
