package com.dandxy.strokes

import com.dandxy.golf.entity.Par
import com.dandxy.golf.input.Handicap
import com.dandxy.strokes.StablefordCalculator.{calculate, numberOfShots}
import org.scalatest.{FlatSpec, Matchers}

class StablefordCalculatorSpec extends FlatSpec with Matchers {

  behavior of "StablefordCalculatorSpec"

  val indexA = 5
  val indexB = 1

  it should "calculate the number of stableford points" in {
    val hA = Handicap(-2)
    calculate(Par.ParFour, hA, indexB, 2).value shouldBe 3
    calculate(Par.ParFour, hA, indexB, 3).value shouldBe 2
    calculate(Par.ParFour, hA, indexB, 4).value shouldBe 1
    calculate(Par.ParFour, hA, indexB, 5).value shouldBe 0
    calculate(Par.ParFour, hA, indexB, 6).value shouldBe 0
    calculate(Par.ParFour, hA, indexB, 7).value shouldBe 0

    val hB = Handicap(0)
    calculate(Par.ParFour, hB, indexB, 2).value shouldBe 4
    calculate(Par.ParFour, hB, indexB, 3).value shouldBe 3
    calculate(Par.ParFour, hB, indexB, 4).value shouldBe 2
    calculate(Par.ParFour, hB, indexB, 5).value shouldBe 1
    calculate(Par.ParFour, hB, indexB, 6).value shouldBe 0
    calculate(Par.ParFour, hB, indexB, 7).value shouldBe 0

    val hC = Handicap(38)
    calculate(Par.ParFour, hC, indexB, 2).value shouldBe 7
    calculate(Par.ParFour, hC, indexB, 3).value shouldBe 6
    calculate(Par.ParFour, hC, indexB, 4).value shouldBe 5
    calculate(Par.ParFour, hC, indexB, 5).value shouldBe 4
    calculate(Par.ParFour, hC, indexB, 6).value shouldBe 3
    calculate(Par.ParFour, hC, indexB, 7).value shouldBe 2
  }

  it should "calculate the number of shots to add onto the par" in {
    numberOfShots(Handicap(-2), indexA) shouldBe 0
    numberOfShots(Handicap(0), indexA) shouldBe 0
    numberOfShots(Handicap(2), indexA) shouldBe 0
    numberOfShots(Handicap(22), indexA) shouldBe 1

    numberOfShots(Handicap(-2), indexB) shouldBe -1
    numberOfShots(Handicap(0), indexB) shouldBe 0
    numberOfShots(Handicap(2), indexB) shouldBe 1
    numberOfShots(Handicap(22), indexB) shouldBe 2
  }
}
