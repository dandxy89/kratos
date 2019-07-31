package com.dandxy.strokes

import com.dandxy.model.golf.entity.Par.{ ParFour, ParThree }
import com.dandxy.strokes.StrokesGainedCalculator._
import com.dandxy.testData.SimulationTestData
import org.scalatest.{ FlatSpec, Matchers }

class PGAExamplesSpec extends FlatSpec with Matchers with SimulationTestData {

  "Strokes Gained: off-the-tee" should "return 0.275" in {
    val strokesGainedAll = getAllStrokesGained(pgaExample)
    getStrokesGainedOffTheTee(strokesGainedAll, ParFour) shouldBe 0.275
  }

  // Strokes Gained: Approach-the-Green
  // Strokes gained: approach-the-green measures player performance on approach shots.
  // Approach shots include all shots that are not from the tee on par-4 and par-5 holes and are not
  // included in strokes gained: around-the-green and strokes gained: putting. Approach shots include tee shots on par-3s.
  "Strokes Gained: approach-the-green" should "return 0.274" in {
    val strokesGainedAll = getAllStrokesGained(pgaExample)
    getStrokesGainedApproachTheGreen(strokesGainedAll, ParThree) shouldBe 0.274
  }

  // Strokes Gained: Around-the-Green
  // Strokes gained: around-the-green measures player performance on any shot within 30 yards of the edge of the green.
  // This statistic does not include any shots taken on the putting green.
  "Strokes Gained: around-the-green" should "return 0.0" in {
    val strokesGainedAll = getAllStrokesGained(pgaExample)
    getStrokesGainedAroundTheGreen(strokesGainedAll) shouldBe 0.0
  }

  "Strokes Gained: Putting" should "return 0.826" in {
    val strokesGainedAll = getAllStrokesGained(pgaExample)
    getStrokesGainedPutting(strokesGainedAll) shouldBe 0.826
  }

  "Strokes Gained: total" should "return 1.1 as the final result" in {
    // Off-the-tee + approach-the-green + around-the-green + putting = strokes gained: total
    val strokesGainedAll = getAllStrokesGained(pgaExample)
    strokesGainedAll.map(_.result).sum shouldBe 1.1
  }

  "Calculate" should "return all of the metrics in one case class" in {
    calculate(dbCalled)(pgaExample.map(_.data), ParFour).unsafeRunSync() shouldBe pgaExpectedResult
  }
}
