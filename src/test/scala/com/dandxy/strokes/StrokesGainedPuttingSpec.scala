package com.dandxy.strokes

import cats.effect.IO
import com.dandxy.model.error.DomainError.{InvalidDistance, StatisticNotKnown}
import com.dandxy.model.golf.Distance
import com.dandxy.model.golf.DistanceMetric.{Feet, Meters, Yards}
import com.dandxy.model.golf.Location.OnTheGreen
import com.dandxy.model.golf.Par.ParThree
import com.dandxy.strokes.StrokesGainedPutting._
import org.scalatest.{FlatSpec, Matchers}

class StrokesGainedPuttingSpec extends FlatSpec with Matchers with SimulationTestData {

  behavior of "Strokes Gained Putting"

  def runIO[A](io: IO[A]): A = io.unsafeRunSync()

  "findPuttingStrokes" should "find all of the putting strokes" in {
    findPuttingStrokes(parThreeExample) shouldBe parThreeExample.drop(1)
  }

  "conversion" should "convert to correct metric" in {
    conversion(Distance(1), Feet) shouldBe Distance(1.0)
    conversion(Distance(1), Meters) shouldBe Distance(3.28084)
    conversion(Distance(1), Yards) shouldBe Distance(3.0)
  }

  "calculate" should "determine the correct number of strokes gained / lost" in {
    runIO(calculate(2, Distance(-1), dbCalled(OnTheGreen)(Distance(10)))) shouldBe Left(InvalidDistance(Distance(-1)))
    runIO(calculate(2, Distance(101), dbCalled(OnTheGreen)(Distance(10)))) shouldBe Left(StatisticNotKnown(Distance(101)))
    runIO(calculate(2, Distance(10), dbCalled(OnTheGreen)(Distance(10)))) shouldBe Right(0.3740000000000001)
  }

  "getStrokesGained" should "run the db call and " in {
    runIO(getStrokesGained(dbCalled(OnTheGreen))(testUserInput(parThreeExample, ParThree))) shouldBe Right(0.3740000000000001)
    runIO(getStrokesGained(dbCalled(OnTheGreen))(testUserInput(parThreeExampleBadGolfer, ParThree))) shouldBe Right(2.374)
    runIO(getStrokesGained(dbCalled(OnTheGreen))(testUserInput(parThreeExampleGoodGolfer, ParThree))) shouldBe Right(-0.6259999999999999)
  }
}
