package com.dandxy.strokes

import com.dandxy.model.golf.entity.Par.{ ParFive, ParFour, ParThree }
import com.dandxy.strokes.StrokesGainedCalculator._
import com.dandxy.testData.SimulationTestData
import org.scalatest.{ FlatSpec, Matchers }

class CalculateStrokesGainedSpec extends FlatSpec with Matchers with SimulationTestData {

  behavior of "Calculate Strokes Gained"

  it should "parThreeExample" in {
    runIO(calculate(dbCalled)(parThreeExample, ParThree)) shouldBe expectedParThreeExample
  }

  it should "parThreeExampleBadGolfer" in {
    runIO(calculate(dbCalled)(parThreeExampleBadGolfer, ParThree)) shouldBe expectedParThreeExampleBadGolfer
  }

  it should "parThreeExampleGoodGolfer" in {
    runIO(calculate(dbCalled)(parThreeExampleGoodGolfer, ParThree)) shouldBe expectedParThreeExampleGoodGolfer
  }

  it should "parFourExample" in {
    runIO(calculate(dbCalled)(parFourExample, ParFour)) shouldBe expectedParFourExample
  }

  it should "parFourExampleHoleInOne" in {
    runIO(calculate(dbCalled)(parFourExampleHoleInOne, ParFour)) shouldBe expectedParFourExampleHoleInOne
  }

  it should "parFiveExample" in {
    runIO(calculate(dbCalled)(parFiveExample, ParFive)) shouldBe expectedParFiveExample
  }

  it should "parFiveExampleBigDrive" in {
    runIO(calculate(dbCalled)(parFiveExampleBigDrive, ParFive)) shouldBe expectedParFiveExampleBigDrive
  }
}
