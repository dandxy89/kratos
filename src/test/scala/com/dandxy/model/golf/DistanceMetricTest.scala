package com.dandxy.model.golf

import com.dandxy.model.golf.DistanceMeasurement.{ Feet, Meters, Yards }
import org.scalatest.{ FlatSpec, Matchers }

class DistanceMetricTest extends FlatSpec with Matchers {

  behavior of "toYards function"

  it should "correctly convert from yards" in {
    Yards.toYards(5) shouldBe 5.0
  }

  it should "correctly convert from feet" in {
    Yards.toYards(5) shouldBe 5.0
  }

  it should "correctly convert from meters" in {
    Meters.toYards(5) shouldBe 5.4680666666665
  }

  behavior of "toFeet function"

  it should "correctly convert from yards" in {
    Yards.toFeet(5) shouldBe 15.0
  }

  it should "correctly convert from feet" in {
    Feet.toFeet(5) shouldBe 5.0
  }

  it should "correctly convert from meters" in {
    Feet.toFeet(5) shouldBe 5.0
  }

  behavior of "toMeters function"

  it should "correctly convert from yards" in {
    Yards.toMeters(5) shouldBe 4.572
  }

  it should "correctly convert from feet" in {
    Feet.toMeters(5) shouldBe 1.524
  }

  it should "correctly convert from meters" in {
    Meters.toMeters(5) shouldBe 5.0
  }

}
