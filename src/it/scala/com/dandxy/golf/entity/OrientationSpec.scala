package com.dandxy.golf.entity

import com.dandxy.golf.entity.Orientation._
import org.scalatest.{ FlatSpec, Matchers }

class OrientationSpec extends FlatSpec with Matchers {

  behavior of "OrientationSpec"

  it should "fromId" in {
    Orientation.fromId(1) shouldBe ShortLeft
    Orientation.fromId(2) shouldBe Short
    Orientation.fromId(3) shouldBe ShortRight
    Orientation.fromId(4) shouldBe MiddleLeft
    Orientation.fromId(5) shouldBe Middle
    Orientation.fromId(6) shouldBe MiddleRight
    Orientation.fromId(7) shouldBe LongLeft
    Orientation.fromId(8) shouldBe Long
    Orientation.fromId(111) shouldBe LongRight
  }
}
