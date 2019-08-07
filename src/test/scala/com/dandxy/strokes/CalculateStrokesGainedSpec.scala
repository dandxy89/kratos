package com.dandxy.strokes

import com.dandxy.strokes.StrokesGainedCalculator._
import com.dandxy.testData.SimulationTestData
import org.scalatest.{ FlatSpec, Matchers }

class CalculateStrokesGainedSpec extends FlatSpec with Matchers with SimulationTestData {

  behavior of "Calculate Strokes Gained"

  it should "parThreeExample" in {
    runIO(calculate(dbCalled)(parThreeExample)) shouldBe expectedParThreeExample
  }

  it should "parThreeExampleBadGolfer" in {
    runIO(calculate(dbCalled)(parThreeExampleBadGolfer)) shouldBe expectedParThreeExampleBadGolfer
  }

  it should "parThreeExampleGoodGolfer" in {
    runIO(calculate(dbCalled)(parThreeExampleGoodGolfer)) shouldBe expectedParThreeExampleGoodGolfer
  }

  it should "parFourExample" in {
    runIO(calculate(dbCalled)(parFourExample)) shouldBe expectedParFourExample
  }

  it should "parFourExampleHoleInOne" in {
    runIO(calculate(dbCalled)(parFourExampleHoleInOne)) shouldBe expectedParFourExampleHoleInOne
  }

  it should "parFiveExample" in {
    runIO(calculate(dbCalled)(parFiveExample)) shouldBe expectedParFiveExample
  }

  it should "parFiveExampleBigDrive" in {
    runIO(calculate(dbCalled)(parFiveExampleBigDrive)) shouldBe expectedParFiveExampleBigDrive
  }

  it should "parFiveExampleBigDriveOutofBounds" in {
    runIO(calculate(dbCalled)(parFiveExampleBigDriveOutOfBounds)) shouldBe expectedParFiveExampleBigDriveOutOfBounds
  }
}
