package com.dandxy.testData

import cats.effect.IO
import com.dandxy.model.golf.input.DistanceMeasurement._
import com.dandxy.model.golf.entity.GolfClub._
import com.dandxy.model.golf.entity.Location.{Fairway, OnTheGreen, TeeBox}
import com.dandxy.model.golf.entity.Orientation._
import com.dandxy.model.golf.entity.Score._
import com.dandxy.model.golf.entity.{Hole, Location, Par}
import com.dandxy.model.golf.input.GolfInput.{HoleInput, UserGameInput}
import com.dandxy.model.golf.input.{Distance, HoleResult}
import com.dandxy.model.golf.pga.Statistic.PGAStatistic
import com.dandxy.strokes.StrokesGainedCalculator.InputAndMetric

trait SimulationTestData {

  def runIO[A](io: IO[A]): A = io.unsafeRunSync()

  def testUserInput(inputData: List[HoleInput], holePar: Par) =
    UserGameInput("testID123", Feet, Hole(1), holePar, inputData)

  val dbCalled: Location => Distance => IO[PGAStatistic] = stat =>
    distance =>
      stat match {
        case OnTheGreen =>
          distance match {
            case Distance(17) => IO.pure(PGAStatistic(Distance(17), 1.83))
            case Distance(10) => IO.pure(PGAStatistic(Distance(10), 1.626))
            case Distance(2)  => IO.pure(PGAStatistic(Distance(2), 1.009))
            case _ =>
              println(s"Putting Miss: $distance")
              IO.pure(PGAStatistic(Distance(10), 1.626))
          }

        case TeeBox =>
          distance match {
            case Distance(446.0) => IO.pure(PGAStatistic(Distance(446), 4.10))
            case Distance(430.0) => IO.pure(PGAStatistic(Distance(430), 4.08))
            case Distance(160.0) => IO.pure(PGAStatistic(Distance(160), 2.99))
            case Distance(559.0) => IO.pure(PGAStatistic(Distance(560), 4.74))
            case Distance(310.0) => IO.pure(PGAStatistic(Distance(310), 3.75))
            case Distance(210.0) => IO.pure(PGAStatistic(Distance(210), 3.15))
            case _ =>
              println(s"Tee Miss: $distance")
              IO.pure(PGAStatistic(Distance(100), 0))
          }

        case _ =>
          distance match {
            case Distance(116.0) => IO.pure(PGAStatistic(Distance(116), 2.825))
            case Distance(430.0) => IO.pure(PGAStatistic(Distance(430), 4.08))
            case Distance(320.0) => IO.pure(PGAStatistic(Distance(320), 3.84))
            case Distance(160.0) => IO.pure(PGAStatistic(Distance(160), 2.98))
            case Distance(200.0) => IO.pure(PGAStatistic(Distance(200), 3.19))
            case _ =>
              println(s"Other Miss: $distance")
              IO.pure(PGAStatistic(Distance(100), 0))
          }
      }

  val parThreeExample: List[HoleInput] = List(
    HoleInput(Distance(210), 1, TeeBox, Option(MiddleLeft), FourIron),
    HoleInput(Distance(10), 2, OnTheGreen, Option(LongLeft), Putter),
    HoleInput(Distance(2), 3, OnTheGreen, Option(MiddleLeft), Putter)
  )

  val expectedParThreeExample = HoleResult(
    score = ParredHole(3),
    strokesGained = 0.15000000000000002,
    strokesGainedOffTheTee = 0.0,
    strokesGainedApproach = 0.524,
    strokesGainedAround = 0.0,
    strokesGainedPutting = -0.374,
    userDate = List(
      HoleInput(Distance(210.0), 1, TeeBox, Some(MiddleLeft), FourIron),
      HoleInput(Distance(10.0), 2, OnTheGreen, Some(LongLeft), Putter),
      HoleInput(Distance(2.0), 3, OnTheGreen, Some(MiddleLeft), Putter)
    )
  )

  val parThreeExampleBadGolfer: List[HoleInput] = List(
    HoleInput(Distance(210), 1, TeeBox, Option(MiddleLeft), FourIron),
    HoleInput(Distance(10), 2, OnTheGreen, Option(LongLeft), Putter),
    HoleInput(Distance(2), 3, OnTheGreen, Option(MiddleLeft), Putter),
    HoleInput(Distance(2), 4, OnTheGreen, Option(MiddleLeft), Putter),
    HoleInput(Distance(2), 5, OnTheGreen, Option(MiddleLeft), Putter)
  )

  val expectedParThreeExampleBadGolfer = HoleResult(
    score = DoubleBogey(5),
    strokesGained = -1.85,
    strokesGainedOffTheTee = 0.0,
    strokesGainedApproach = 0.524,
    strokesGainedAround = 0.0,
    strokesGainedPutting = -2.374,
    userDate = List(
      HoleInput(Distance(210.0), 1, TeeBox, Some(MiddleLeft), FourIron),
      HoleInput(Distance(10.0), 2, OnTheGreen, Some(LongLeft), Putter),
      HoleInput(Distance(2.0), 3, OnTheGreen, Some(MiddleLeft), Putter),
      HoleInput(Distance(2.0), 4, OnTheGreen, Some(MiddleLeft), Putter),
      HoleInput(Distance(2.0), 5, OnTheGreen, Some(MiddleLeft), Putter)
    )
  )

  val parThreeExampleGoodGolfer: List[HoleInput] = List(
    HoleInput(Distance(210), 1, TeeBox, Option(MiddleLeft), FourIron),
    HoleInput(Distance(10), 2, OnTheGreen, Option(LongLeft), Putter)
  )

  val expectedParThreeExampleGoodGolfer = HoleResult(
    score = Birdie(2),
    strokesGained = 1.15,
    strokesGainedOffTheTee = 0.0,
    strokesGainedApproach = 0.524,
    strokesGainedAround = 0.0,
    strokesGainedPutting = 0.626,
    userDate = List(
      HoleInput(Distance(210.0), 1, TeeBox, Some(MiddleLeft), FourIron),
      HoleInput(Distance(10.0), 2, OnTheGreen, Some(LongLeft), Putter)
    )
  )

  val parFourExample: List[HoleInput] = List(
    HoleInput(Distance(430), 1, TeeBox, Option(MiddleLeft), Driver),
    HoleInput(Distance(160), 2, Fairway, Option(MiddleLeft), FiveIron),
    HoleInput(Distance(10), 3, OnTheGreen, Option(LongLeft), Putter),
    HoleInput(Distance(2), 4, OnTheGreen, Option(MiddleLeft), Putter)
  )

  val expectedParFourExample = HoleResult(
    score = ParredHole(4),
    strokesGained = 0.07999999999999995,
    strokesGainedOffTheTee = 0.1,
    strokesGainedApproach = 0.354,
    strokesGainedAround = 0.0,
    strokesGainedPutting = -0.374,
    userDate = List(
      HoleInput(Distance(430.0), 1, TeeBox, Some(MiddleLeft), Driver),
      HoleInput(Distance(160.0), 2, Fairway, Some(MiddleLeft), FiveIron),
      HoleInput(Distance(10.0), 3, OnTheGreen, Some(LongLeft), Putter),
      HoleInput(Distance(2.0), 4, OnTheGreen, Some(MiddleLeft), Putter)
    )
  )

  val parFourExampleHoleInOne: List[HoleInput] = List(
    HoleInput(Distance(430), 1, TeeBox, Option(MiddleLeft), Driver)
  )

  val expectedParFourExampleHoleInOne =
    HoleResult(
      score = HoleInOne(1),
      strokesGained = 3.08,
      strokesGainedOffTheTee = 3.08,
      strokesGainedApproach = 0.0,
      strokesGainedAround = 0.0,
      strokesGainedPutting = 0.0,
      userDate = List(HoleInput(Distance(430.0), 1, TeeBox, Some(MiddleLeft), Driver))
    )

  val parFiveExample: List[HoleInput] = List(
    HoleInput(Distance(559), 1, TeeBox, Option(MiddleLeft), Driver),
    HoleInput(Distance(320), 2, Fairway, Option(MiddleLeft), FiveWood),
    HoleInput(Distance(160), 3, Fairway, Option(MiddleLeft), FourIron),
    HoleInput(Distance(10), 4, OnTheGreen, Option(LongLeft), Putter),
    HoleInput(Distance(2), 5, OnTheGreen, Option(MiddleLeft), Putter)
  )

  val expectedParFiveExample = HoleResult(
    score = ParredHole(5),
    strokesGained = -0.26,
    strokesGainedOffTheTee = -0.1,
    strokesGainedApproach = 0.21399999999999997,
    strokesGainedAround = 0.0,
    strokesGainedPutting = -0.374,
    userDate = List(
      HoleInput(Distance(559.0), 1, TeeBox, Some(MiddleLeft), Driver),
      HoleInput(Distance(320.0), 2, Fairway, Some(MiddleLeft), FiveWood),
      HoleInput(Distance(160.0), 3, Fairway, Some(MiddleLeft), FourIron),
      HoleInput(Distance(10.0), 4, OnTheGreen, Some(LongLeft), Putter),
      HoleInput(Distance(2.0), 5, OnTheGreen, Some(MiddleLeft), Putter)
    )
  )

  val parFiveExampleBigDrive: List[HoleInput] = List(
    HoleInput(Distance(559), 1, TeeBox, Option(MiddleLeft), Driver),
    HoleInput(Distance(200), 2, Fairway, Option(MiddleLeft), FiveWood),
    HoleInput(Distance(160), 3, Fairway, Option(MiddleLeft), FourIron),
    HoleInput(Distance(10), 4, OnTheGreen, Option(LongLeft), Putter),
    HoleInput(Distance(2), 5, OnTheGreen, Option(MiddleLeft), Putter)
  )

  val expectedParFiveExampleBigDrive = HoleResult(
    score = ParredHole(5),
    strokesGained = -0.26,
    strokesGainedOffTheTee = 0.55,
    strokesGainedApproach = -0.43600000000000005,
    strokesGainedAround = 0.0,
    strokesGainedPutting = -0.374,
    userDate = List(
      HoleInput(Distance(559.0), 1, TeeBox, Some(MiddleLeft), Driver),
      HoleInput(Distance(200.0), 2, Fairway, Some(MiddleLeft), FiveWood),
      HoleInput(Distance(160.0), 3, Fairway, Some(MiddleLeft), FourIron),
      HoleInput(Distance(10.0), 4, OnTheGreen, Some(LongLeft), Putter),
      HoleInput(Distance(2.0), 5, OnTheGreen, Some(MiddleLeft), Putter)
    )
  )

  val pgaExample: List[InputAndMetric] = List(
    InputAndMetric(HoleInput(Distance(446), 1, TeeBox, Option(MiddleLeft), Driver), PGAStatistic(Distance(446), 4.100), 0),
    InputAndMetric(HoleInput(Distance(116), 2, Fairway, Option(MiddleLeft), FiveWood), PGAStatistic(Distance(116), 2.825), 0),
    InputAndMetric(HoleInput(Distance(17), 4, OnTheGreen, Option(LongLeft), Putter), PGAStatistic(Distance(17), 1.826), 0)
  )

  val pgaExpectedResult: HoleResult =
    HoleResult(
      score = Birdie(3),
      strokesGained = 1.1,
      strokesGainedOffTheTee = 0.275,
      strokesGainedApproach = -0.005,
      strokesGainedAround = 0.0,
      strokesGainedPutting = 0.83,
      userDate = List(
        HoleInput(Distance(446.0), 1, TeeBox, Some(MiddleLeft), Driver),
        HoleInput(Distance(116.0), 2, Fairway, Some(MiddleLeft), FiveWood),
        HoleInput(Distance(17.0), 4, OnTheGreen, Some(LongLeft), Putter)
      )
    )
}
