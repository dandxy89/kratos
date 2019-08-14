package com.dandxy.strokes

import com.dandxy.golf.input.Handicap
import com.dandxy.model.user.Identifier.Hole
import com.dandxy.strokes.StrokesGainedCalculator._
import com.dandxy.testData.SimulationTestData
import org.scalatest.{FlatSpec, Matchers}

class CalculateStrokesGainedSpec extends FlatSpec with Matchers with SimulationTestData {

  behavior of "Calculate Strokes Gained"

  val myHandicap: Handicap = Handicap(7.5)

  it should "parThreeExample" in {
    runIO(calculate(dbCalled)(myHandicap, parThreeExample, Some(Hole(1)))) shouldBe expectedParThreeExample
  }

  it should "parThreeExampleBadGolfer" in {
    runIO(calculate(dbCalled)(myHandicap, parThreeExampleBadGolfer, Some(Hole(2)))) shouldBe expectedParThreeExampleBadGolfer
  }

  it should "parThreeExampleGoodGolfer" in {
    runIO(calculate(dbCalled)(myHandicap, parThreeExampleGoodGolfer, Some(Hole(3)))) shouldBe expectedParThreeExampleGoodGolfer
  }

  it should "parFourExample" in {
    runIO(calculate(dbCalled)(myHandicap, parFourExample, Some(Hole(4)))) shouldBe expectedParFourExample
  }

  it should "parFourExampleHoleInOne" in {
    runIO(calculate(dbCalled)(myHandicap, parFourExampleHoleInOne, Some(Hole(5)))) shouldBe expectedParFourExampleHoleInOne
  }

  it should "parFiveExample" in {
    runIO(calculate(dbCalled)(myHandicap, parFiveExample, Some(Hole(6)))) shouldBe expectedParFiveExample
  }

  it should "parFiveExampleBigDrive" in {
    runIO(calculate(dbCalled)(myHandicap, parFiveExampleBigDrive, Some(Hole(7)))) shouldBe expectedParFiveExampleBigDrive
  }

  it should "parFiveExampleBigDriveOutofBounds" in {
    runIO(calculate(dbCalled)(myHandicap, parFiveExampleBigDriveOutOfBounds, Some(Hole(8)))) shouldBe expectedParFiveExampleBigDriveOutOfBounds
  }
}
