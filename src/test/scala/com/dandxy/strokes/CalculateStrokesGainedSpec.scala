package com.dandxy.strokes

import com.dandxy.model.golf.input.Handicap
import com.dandxy.strokes.StrokesGainedCalculator._
import com.dandxy.testData.SimulationTestData
import org.scalatest.{ FlatSpec, Matchers }

class CalculateStrokesGainedSpec extends FlatSpec with Matchers with SimulationTestData {

  behavior of "Calculate Strokes Gained"

  val myHandicap: Handicap = Handicap(7.5)

  it should "parThreeExample" in {
    runIO(calculate(dbCalled)(myHandicap, parThreeExample)) shouldBe expectedParThreeExample
  }

  it should "parThreeExampleBadGolfer" in {
    runIO(calculate(dbCalled)(myHandicap, parThreeExampleBadGolfer)) shouldBe expectedParThreeExampleBadGolfer
  }

  it should "parThreeExampleGoodGolfer" in {
    runIO(calculate(dbCalled)(myHandicap, parThreeExampleGoodGolfer)) shouldBe expectedParThreeExampleGoodGolfer
  }

  it should "parFourExample" in {
    runIO(calculate(dbCalled)(myHandicap, parFourExample)) shouldBe expectedParFourExample
  }

  it should "parFourExampleHoleInOne" in {
    runIO(calculate(dbCalled)(myHandicap, parFourExampleHoleInOne)) shouldBe expectedParFourExampleHoleInOne
  }

  it should "parFiveExample" in {
    runIO(calculate(dbCalled)(myHandicap, parFiveExample)) shouldBe expectedParFiveExample
  }

  it should "parFiveExampleBigDrive" in {
    runIO(calculate(dbCalled)(myHandicap, parFiveExampleBigDrive)) shouldBe expectedParFiveExampleBigDrive
  }

  it should "parFiveExampleBigDriveOutofBounds" in {
    runIO(calculate(dbCalled)(myHandicap, parFiveExampleBigDriveOutOfBounds)) shouldBe expectedParFiveExampleBigDriveOutOfBounds
  }
}
