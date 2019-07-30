package com.dandxy.strokes

import com.dandxy.model.golf.Distance
import com.dandxy.model.golf.Par.{ ParFive, ParFour, ParThree }
import com.dandxy.model.golf.Statistic.PGAStatistic
import org.scalatest.{ FlatSpec, Matchers }

class StrokesGainedOffTheTeeSpec extends FlatSpec with Matchers with SimulationTestData {

  val testClass = new StrokesGainedOffTheTee(dbCalled)

  behavior of "Strokes Gained off the tee"

  "isEligible" should "determine if Strokes Gained off tee can be calculated or not" in {
    testClass.isEligible(testUserInput(parThreeExample, ParThree)) shouldBe true
    testClass.isEligible(testUserInput(parFourExample, ParFour)) shouldBe false
    testClass.isEligible(testUserInput(parFiveExample, ParFive)) shouldBe false
  }

  "calculate" should "correctly calculate the Strokes Gained (or lost)" in {
    testClass.calculate(PGAStatistic(Distance(430), 4.08), None) shouldBe -1.0
    testClass.calculate(PGAStatistic(Distance(430), 4.08), Some(PGAStatistic(Distance(160), 2.99))) shouldBe 0.08999999999999986
  }

  "getStrokesGained" should "correctly calculate strokes gained" in {
    testClass.getStrokesGained(testUserInput(parThreeExample, ParThree)).unsafeRunSync() shouldBe 0.0
    testClass.getStrokesGained(testUserInput(parFourExample, ParFour)).unsafeRunSync() shouldBe 0.10000000000000009
    testClass.getStrokesGained(testUserInput(parFourExampleHoleInOne, ParFour)).unsafeRunSync() shouldBe -1.0
    testClass.getStrokesGained(testUserInput(parFiveExample, ParFive)).unsafeRunSync() shouldBe -0.09999999999999964
    testClass.getStrokesGained(testUserInput(parFiveExampleBigDrive, ParFive)).unsafeRunSync() shouldBe 0.5500000000000003
  }
}
