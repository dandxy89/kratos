package com.dandxy.golf.entity

import com.dandxy.golf.entity.Location._
import com.dandxy.golf.entity.Penalty._
import org.scalatest.{FlatSpec, Matchers}

class LocationSpec extends FlatSpec with Matchers {

  behavior of "LocationSpec"

  it should "fromId" in {
    Location.fromId(1) shouldBe TeeBox
    Location.fromId(2) shouldBe Fairway
    Location.fromId(3) shouldBe Rough
    Location.fromId(4) shouldBe Bunker
    Location.fromId(5) shouldBe Recovery
    Location.fromId(6) shouldBe OnTheGreen
    Location.fromId(7) shouldBe Hazard
    Location.fromId(8) shouldBe OutOfBounds
    Location.fromId(9) shouldBe LostBall
    Location.fromId(10) shouldBe UnplayableLie
    Location.fromId(11) shouldBe OneShotPenalty
    Location.fromId(12) shouldBe TwoShotPenalty
  }
}
