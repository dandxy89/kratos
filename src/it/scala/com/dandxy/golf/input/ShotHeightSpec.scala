package com.dandxy.golf.input

import com.dandxy.golf.input.ShotHeight.{High, Low, Normal, Putt}
import org.scalatest.{FlatSpec, Matchers}

class ShotHeightSpec extends FlatSpec with Matchers {

  behavior of "ShotHeightSpec"

  it should "fromId" in {
    ShotHeight.fromId(1) shouldBe Low
    ShotHeight.fromId(2) shouldBe Normal
    ShotHeight.fromId(3) shouldBe High
    ShotHeight.fromId(4) shouldBe Putt
  }
}
