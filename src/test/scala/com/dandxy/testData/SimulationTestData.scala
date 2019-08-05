package com.dandxy.testData

import cats.effect.IO
import com.dandxy.model.golf.entity.GolfClub._
import com.dandxy.model.golf.entity.Location.{ Fairway, OnTheGreen, TeeBox }
import com.dandxy.model.golf.entity.Orientation._
import com.dandxy.model.golf.entity.Penalty.OutOfBounds
import com.dandxy.model.golf.entity.Score._
import com.dandxy.model.golf.entity.{ Hole, Location, Par }
import com.dandxy.model.golf.input.DistanceMeasurement._
import com.dandxy.model.golf.input.GolfInput.{ ShotInput, UserCourseInput, UserHoleInput }
import com.dandxy.model.golf.input.{ Distance, HoleResult }
import com.dandxy.model.golf.pga.Statistic.PGAStatistic
import com.dandxy.model.player.PlayerId
import com.dandxy.strokes.StrokesGainedCalculator.InputAndMetric

trait SimulationTestData {

  def runIO[A](io: IO[A]): A = io.unsafeRunSync()

  def testUserInput(inputData: List[ShotInput], holePar: Par) =
    UserHoleInput(PlayerId(1), Feet, Hole(1), holePar, inputData, 0, None, None, None, None)

  val generateCompleteHoleData: (Hole, Par, List[ShotInput]) => UserHoleInput = (h, p, in) =>
    UserHoleInput(
      playerId = PlayerId(1),
      puttingMetric = Feet,
      hole = h,
      par = p,
      golfInput = in,
      handicap = 5,
      ballUsed = None,
      greenSpeed = None,
      temperature = None,
      windSpeed = None
    )

  //val generateCompleteGameData: List[UserHoleInput] => UserCourseInput = inputs => UserCourseInput("Test golf club", inputs)

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

  val parThreeExample: List[ShotInput] = List(
    ShotInput(Distance(210), 1, TeeBox, Option(MiddleLeft), FourIron, None, None, 1),
    ShotInput(Distance(10), 2, OnTheGreen, Option(LongLeft), Putter, None, None, 1),
    ShotInput(Distance(2), 3, OnTheGreen, Option(MiddleLeft), Putter, None, None, 1)
  )

  val expectedParThreeExample = HoleResult(
    score = ParredHole(3),
    strokesGained = 0.15000000000000002,
    strokesGainedOffTheTee = 0.0,
    strokesGainedApproach = 0.524,
    strokesGainedAround = 0.0,
    strokesGainedPutting = -0.374,
    userDate = List(
      ShotInput(Distance(210.0), 1, TeeBox, Some(MiddleLeft), FourIron, None, None, 1),
      ShotInput(Distance(10.0), 2, OnTheGreen, Some(LongLeft), Putter, None, None, 1),
      ShotInput(Distance(2.0), 3, OnTheGreen, Some(MiddleLeft), Putter, None, None, 1)
    )
  )

  val parThreeExampleBadGolfer: List[ShotInput] = List(
    ShotInput(Distance(210), 1, TeeBox, Option(MiddleLeft), FourIron, None, None, 1),
    ShotInput(Distance(10), 2, OnTheGreen, Option(LongLeft), Putter, None, None, 1),
    ShotInput(Distance(2), 3, OnTheGreen, Option(MiddleLeft), Putter, None, None, 1),
    ShotInput(Distance(2), 4, OnTheGreen, Option(MiddleLeft), Putter, None, None, 1),
    ShotInput(Distance(2), 5, OnTheGreen, Option(MiddleLeft), Putter, None, None, 1)
  )

  val expectedParThreeExampleBadGolfer = HoleResult(
    score = DoubleBogey(5),
    strokesGained = -1.85,
    strokesGainedOffTheTee = 0.0,
    strokesGainedApproach = 0.524,
    strokesGainedAround = 0.0,
    strokesGainedPutting = -2.374,
    userDate = List(
      ShotInput(Distance(210.0), 1, TeeBox, Some(MiddleLeft), FourIron, None, None, 1),
      ShotInput(Distance(10.0), 2, OnTheGreen, Some(LongLeft), Putter, None, None, 1),
      ShotInput(Distance(2.0), 3, OnTheGreen, Some(MiddleLeft), Putter, None, None, 1),
      ShotInput(Distance(2.0), 4, OnTheGreen, Some(MiddleLeft), Putter, None, None, 1),
      ShotInput(Distance(2.0), 5, OnTheGreen, Some(MiddleLeft), Putter, None, None, 1)
    )
  )

  val parThreeExampleGoodGolfer: List[ShotInput] = List(
    ShotInput(Distance(210), 1, TeeBox, Option(MiddleLeft), FourIron, None, None, 1),
    ShotInput(Distance(10), 2, OnTheGreen, Option(LongLeft), Putter, None, None, 1)
  )

  val expectedParThreeExampleGoodGolfer = HoleResult(
    score = Birdie(2),
    strokesGained = 1.15,
    strokesGainedOffTheTee = 0.0,
    strokesGainedApproach = 0.524,
    strokesGainedAround = 0.0,
    strokesGainedPutting = 0.626,
    userDate = List(
      ShotInput(Distance(210.0), 1, TeeBox, Some(MiddleLeft), FourIron, None, None, 1),
      ShotInput(Distance(10.0), 2, OnTheGreen, Some(LongLeft), Putter, None, None, 1)
    )
  )

  val parFourExample: List[ShotInput] = List(
    ShotInput(Distance(430), 1, TeeBox, Option(MiddleLeft), Driver, None, None, 1),
    ShotInput(Distance(160), 2, Fairway, Option(MiddleLeft), FiveIron, None, None, 1),
    ShotInput(Distance(10), 3, OnTheGreen, Option(LongLeft), Putter, None, None, 1),
    ShotInput(Distance(2), 4, OnTheGreen, Option(MiddleLeft), Putter, None, None, 1)
  )

  val expectedParFourExample = HoleResult(
    score = ParredHole(4),
    strokesGained = 0.07999999999999995,
    strokesGainedOffTheTee = 0.1,
    strokesGainedApproach = 0.354,
    strokesGainedAround = 0.0,
    strokesGainedPutting = -0.374,
    userDate = List(
      ShotInput(Distance(430.0), 1, TeeBox, Some(MiddleLeft), Driver, None, None, 1),
      ShotInput(Distance(160.0), 2, Fairway, Some(MiddleLeft), FiveIron, None, None, 1),
      ShotInput(Distance(10.0), 3, OnTheGreen, Some(LongLeft), Putter, None, None, 1),
      ShotInput(Distance(2.0), 4, OnTheGreen, Some(MiddleLeft), Putter, None, None, 1)
    )
  )

  val parFourExampleHoleInOne: List[ShotInput] = List(
    ShotInput(Distance(430), 1, TeeBox, Option(MiddleLeft), Driver, None, None, 1)
  )

  val expectedParFourExampleHoleInOne =
    HoleResult(
      score = HoleInOne(1),
      strokesGained = 3.08,
      strokesGainedOffTheTee = 3.08,
      strokesGainedApproach = 0.0,
      strokesGainedAround = 0.0,
      strokesGainedPutting = 0.0,
      userDate = List(ShotInput(Distance(430.0), 1, TeeBox, Some(MiddleLeft), Driver, None, None, 1))
    )

  val parFiveExample: List[ShotInput] = List(
    ShotInput(Distance(559), 1, TeeBox, Option(MiddleLeft), Driver, None, None, 1),
    ShotInput(Distance(320), 2, Fairway, Option(MiddleLeft), FiveWood, None, None, 1),
    ShotInput(Distance(160), 3, Fairway, Option(MiddleLeft), FourIron, None, None, 1),
    ShotInput(Distance(10), 4, OnTheGreen, Option(LongLeft), Putter, None, None, 1),
    ShotInput(Distance(2), 5, OnTheGreen, Option(MiddleLeft), Putter, None, None, 1)
  )

  val expectedParFiveExample = HoleResult(
    score = ParredHole(5),
    strokesGained = -0.26,
    strokesGainedOffTheTee = -0.1,
    strokesGainedApproach = 0.21399999999999997,
    strokesGainedAround = 0.0,
    strokesGainedPutting = -0.374,
    userDate = List(
      ShotInput(Distance(559.0), 1, TeeBox, Some(MiddleLeft), Driver, None, None, 1),
      ShotInput(Distance(320.0), 2, Fairway, Some(MiddleLeft), FiveWood, None, None, 1),
      ShotInput(Distance(160.0), 3, Fairway, Some(MiddleLeft), FourIron, None, None, 1),
      ShotInput(Distance(10.0), 4, OnTheGreen, Some(LongLeft), Putter, None, None, 1),
      ShotInput(Distance(2.0), 5, OnTheGreen, Some(MiddleLeft), Putter, None, None, 1)
    )
  )

  val parFiveExampleBigDrive: List[ShotInput] = List(
    ShotInput(Distance(559), 1, TeeBox, Option(MiddleLeft), Driver, None, None, 1),
    ShotInput(Distance(200), 2, Fairway, Option(MiddleLeft), FiveWood, None, None, 1),
    ShotInput(Distance(160), 3, Fairway, Option(MiddleLeft), FourIron, None, None, 1),
    ShotInput(Distance(10), 4, OnTheGreen, Option(LongLeft), Putter, None, None, 1),
    ShotInput(Distance(2), 5, OnTheGreen, Option(MiddleLeft), Putter, None, None, 1)
  )

  val expectedParFiveExampleBigDrive = HoleResult(
    score = ParredHole(5),
    strokesGained = -0.26,
    strokesGainedOffTheTee = 0.55,
    strokesGainedApproach = -0.43600000000000005,
    strokesGainedAround = 0.0,
    strokesGainedPutting = -0.374,
    userDate = List(
      ShotInput(Distance(559.0), 1, TeeBox, Some(MiddleLeft), Driver, None, None, 1),
      ShotInput(Distance(200.0), 2, Fairway, Some(MiddleLeft), FiveWood, None, None, 1),
      ShotInput(Distance(160.0), 3, Fairway, Some(MiddleLeft), FourIron, None, None, 1),
      ShotInput(Distance(10.0), 4, OnTheGreen, Some(LongLeft), Putter, None, None, 1),
      ShotInput(Distance(2.0), 5, OnTheGreen, Some(MiddleLeft), Putter, None, None, 1)
    )
  )

  val parFiveExampleBigDriveOutOfBounds: List[ShotInput] = List(
    ShotInput(Distance(559), 1, TeeBox, Option(MiddleLeft), Driver, None, None, 1),
    ShotInput(Distance(559), 1, OutOfBounds, Option(MiddleLeft), Driver, None, None, 1),
    ShotInput(Distance(559), 1, TeeBox, Option(MiddleLeft), Driver, None, None, 1),
    ShotInput(Distance(200), 2, Fairway, Option(MiddleLeft), FiveWood, None, None, 1),
    ShotInput(Distance(160), 3, Fairway, Option(MiddleLeft), FourIron, None, None, 1),
    ShotInput(Distance(10), 4, OnTheGreen, Option(LongLeft), Putter, None, None, 1),
    ShotInput(Distance(2), 5, OnTheGreen, Option(MiddleLeft), Putter, None, None, 1)
  )

  val expectedParFiveExampleBigDriveOutOfBounds = HoleResult(
    score = DoubleBogey(7),
    strokesGained = -1.2600000000000002,
    strokesGainedOffTheTee = -1.0,
    strokesGainedApproach = -0.43600000000000005,
    strokesGainedAround = 0.0,
    strokesGainedPutting = -0.374,
    userDate = List(
      ShotInput(Distance(559.0), 1, TeeBox, Some(MiddleLeft), Driver, None, None, 1),
      ShotInput(Distance(559.0), 1, OutOfBounds, Some(MiddleLeft), Driver, None, None, 1),
      ShotInput(Distance(559.0), 1, TeeBox, Some(MiddleLeft), Driver, None, None, 1),
      ShotInput(Distance(200.0), 2, Fairway, Some(MiddleLeft), FiveWood, None, None, 1),
      ShotInput(Distance(160.0), 3, Fairway, Some(MiddleLeft), FourIron, None, None, 1),
      ShotInput(Distance(10.0), 4, OnTheGreen, Some(LongLeft), Putter, None, None, 1),
      ShotInput(Distance(2.0), 5, OnTheGreen, Some(MiddleLeft), Putter, None, None, 1)
    )
  )

  val pgaExample: List[InputAndMetric] = List(
    InputAndMetric(
      ShotInput(Distance(446), 1, TeeBox, Option(MiddleLeft), Driver, None, None, 1),
      PGAStatistic(Distance(446), 4.100),
      0
    ),
    InputAndMetric(
      ShotInput(Distance(116), 2, Fairway, Option(MiddleLeft), FiveWood, None, None, 1),
      PGAStatistic(Distance(116), 2.825),
      0
    ),
    InputAndMetric(
      ShotInput(Distance(17), 4, OnTheGreen, Option(LongLeft), Putter, None, None, 1),
      PGAStatistic(Distance(17), 1.826),
      0
    )
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
        ShotInput(Distance(446.0), 1, TeeBox, Some(MiddleLeft), Driver, None, None, 1),
        ShotInput(Distance(116.0), 2, Fairway, Some(MiddleLeft), FiveWood, None, None, 1),
        ShotInput(Distance(17.0), 4, OnTheGreen, Some(LongLeft), Putter, None, None, 1)
      )
    )
}
