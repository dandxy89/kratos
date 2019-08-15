package com.dandxy.golf.entity

import com.dandxy.golf.entity.Par.{ ParFive, ParFour, ParSeven, ParSix, ParThree }
import org.scalatest.{ FlatSpec, Matchers }

class ParSpec extends FlatSpec with Matchers {

  behavior of "ParSpec"

  it should "fromInt" in {
    Par.fromInt(3) shouldBe ParThree
    Par.fromInt(4) shouldBe ParFour
    Par.fromInt(5) shouldBe ParFive
    Par.fromInt(6) shouldBe ParSix
    Par.fromInt(7) shouldBe ParSeven
  }
}
