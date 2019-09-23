package com.dandxy.util

import cats.implicits._
import org.scalatest.{ FlatSpec, Matchers }

class HelpersSpec extends FlatSpec with Matchers {

  behavior of "HelpersSpec"

  it should "combineAll" in {
    Helpers.combineAll(List(1, 2, 3, 4, 5, 6)) shouldBe 21
    Helpers.combineAll(List("d", "a", "n")) shouldBe "dan"
  }

  it should "roundAt3" in {
    Helpers.roundAt3(3.1267) shouldBe 3.127
  }
}
