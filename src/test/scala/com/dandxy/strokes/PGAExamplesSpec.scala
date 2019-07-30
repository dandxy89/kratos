package com.dandxy.strokes

import com.dandxy.model.golf.Par.ParFour
import org.scalatest.{ FlatSpec, Matchers }

class PGAExamplesSpec extends FlatSpec with Matchers with SimulationTestData {

  "Strokes Gained: off-the-tee" should "return XXX" in {
    val strokesGainedAll = StokesGainedCalculator.getAllStrokesGained(pgaExample)
    StokesGainedCalculator.getStrokesGainedTee(strokesGainedAll, ParFour) shouldBe 0.275
  }

  "Strokes Gained: approach-the-green" should "return XXX" in {
    pending
  }

  "Strokes Gained: around-the-green" should "return XXX" in {
    pending
  }

  "Strokes Gained: Putting" should "return 0.826" in {
    val strokesGainedAll = StokesGainedCalculator.getAllStrokesGained(pgaExample)
    StokesGainedCalculator.getStrokesGainedPutting(strokesGainedAll) shouldBe 0.826
  }

  "Strokes Gained: total" should "return 1.1 as the final result" in {
    // Off-the-tee + approach-the-green + around-the-green + putting = strokes gained: total
    val strokesGainedAll = StokesGainedCalculator.getAllStrokesGained(pgaExample)
    strokesGainedAll.map(_.result).sum shouldBe 1.1
  }

}
