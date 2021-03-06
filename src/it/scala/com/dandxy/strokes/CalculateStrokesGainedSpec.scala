package com.dandxy.strokes

import com.dandxy.golf.input.GolfInput.UserShotInput
import com.dandxy.golf.input.Handicap
import com.dandxy.model.user.Identifier.Hole
import com.dandxy.strokes.StrokesGainedCalculator._
import com.dandxy.testData.SimulationTestData
import org.scalatest.{ FlatSpec, Matchers }

class CalculateStrokesGainedSpec extends FlatSpec with Matchers with SimulationTestData {

  behavior of "Calculate Strokes Gained"

  val myHandicap: Handicap = Handicap(7.5)

  it should "par Three Example" in {
    runIO(calculateOne(dbCalled)(myHandicap, parThreeExample, Some(Hole(1)))) shouldBe expectedParThreeExample
  }

  it should "par Three Example Bad Golfer" in {
    runIO(calculateOne(dbCalled)(myHandicap, parThreeExampleBadGolfer, Some(Hole(2)))) shouldBe expectedParThreeExampleBadGolfer
  }

  it should "par Three Example Good Golfer" in {
    runIO(calculateOne(dbCalled)(myHandicap, parThreeExampleGoodGolfer, Some(Hole(3)))) shouldBe expectedParThreeExampleGoodGolfer
  }

  it should "par Four Example" in {
    runIO(calculateOne(dbCalled)(myHandicap, parFourExample, Some(Hole(4)))) shouldBe expectedParFourExample
  }

  it should "par Four Example Hole In One" in {
    runIO(calculateOne(dbCalled)(myHandicap, parFourExampleHoleInOne, Some(Hole(5)))) shouldBe expectedParFourExampleHoleInOne
  }

  it should "par Five Example" in {
    runIO(calculateOne(dbCalled)(myHandicap, parFiveExample, Some(Hole(6)))) shouldBe expectedParFiveExample
  }

  it should "par Five Example Big Drive" in {
    runIO(calculateOne(dbCalled)(myHandicap, parFiveExampleBigDrive, Some(Hole(7)))) shouldBe expectedParFiveExampleBigDrive
  }

  it should "par Five Example Big Drive Out of Bounds" in {
    runIO(calculateOne(dbCalled)(myHandicap, parFiveExampleBigDriveOutOfBounds, Some(Hole(8)))) shouldBe expectedParFiveExampleBigDriveOutOfBounds
  }

  it should "correctly calculate many holes at once" in {
    val manyHoles: List[UserShotInput] = parThreeExample ++ parFourExample ++ parFiveExample
    runIO(calculateMany(dbCalled)(myHandicap, manyHoles)).size shouldBe 3
  }
}
