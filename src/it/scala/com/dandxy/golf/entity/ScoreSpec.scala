package com.dandxy.golf.entity

import com.dandxy.golf.entity.Score._
import org.scalatest.{ FlatSpec, Matchers }

class ScoreSpec extends FlatSpec with Matchers {

  behavior of "ScoreTest"

  it should "fromId" in {
    Score.fromId(-9) shouldBe HoleInOne
    Score.fromId(-3) shouldBe Albatross
    Score.fromId(-2) shouldBe Eagle
    Score.fromId(-1) shouldBe Birdie
    Score.fromId(0) shouldBe ParredHole
    Score.fromId(1) shouldBe Bogey
    Score.fromId(2) shouldBe DoubleBogey
    Score.fromId(3) shouldBe TripeBogey
    Score.fromId(10) shouldBe MultipleBogey(10)
  }
}
