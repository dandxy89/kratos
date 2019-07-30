package com.dandxy.strokes

import cats.effect.IO
import com.dandxy.model.golf.DistanceMetric.Feet
import com.dandxy.model.golf.GolfClub._
import com.dandxy.model.golf.Location.{ Fairway, OnTheGreen, TeeBox }
import com.dandxy.model.golf.Orientation.{ LongLeft, MiddleLeft }
import com.dandxy.model.golf.Statistic.PGAStatistic
import com.dandxy.model.golf.{ Distance, Hole, Location, Par }
import com.dandxy.model.player.GolfInput.{ UserGolfInput, UserInput }

trait SimulationTestData {

  def runIO[A](io: IO[A]): A = io.unsafeRunSync()

  def testUserInput(inputData: List[UserGolfInput], holePar: Par) =
    UserInput("testID123", Feet, Hole(1), holePar, inputData)

  val dbCalled: Location => Distance => IO[PGAStatistic] = stat =>
    distance =>
      stat match {
        case OnTheGreen => IO.pure(PGAStatistic(Distance(10), 1.626))
        case TeeBox =>
          distance match {
            case Distance(430.0) => IO.pure(PGAStatistic(Distance(430), 4.08))
            case Distance(160.0) => IO.pure(PGAStatistic(Distance(160), 2.99))
            case Distance(559.0) => IO.pure(PGAStatistic(Distance(560), 4.74))
            case Distance(310.0) => IO.pure(PGAStatistic(Distance(310), 3.75))
            case Distance(210.0) => IO.pure(PGAStatistic(Distance(210), 3.15))
            case _               => IO.pure(PGAStatistic(Distance(100), 0))
          }
        case _ =>
          distance match {
            case Distance(430.0) => IO.pure(PGAStatistic(Distance(430), 4.08))
            case Distance(320.0) => IO.pure(PGAStatistic(Distance(320), 3.84))
            case Distance(160.0) => IO.pure(PGAStatistic(Distance(160), 2.98))
            case Distance(200.0) => IO.pure(PGAStatistic(Distance(200), 3.19))
            case _               => IO.pure(PGAStatistic(Distance(100), 0))
          }
      }

  val parThreeExample: List[UserGolfInput] = List(
    UserGolfInput(Distance(205), 1, TeeBox, Option(MiddleLeft), FourIron),
    UserGolfInput(Distance(10), 2, OnTheGreen, Option(LongLeft), Putter),
    UserGolfInput(Distance(2), 3, OnTheGreen, Option(MiddleLeft), Putter)
  )

  val parThreeExampleBadGolfer: List[UserGolfInput] = List(
    UserGolfInput(Distance(205), 1, TeeBox, Option(MiddleLeft), FourIron),
    UserGolfInput(Distance(10), 2, OnTheGreen, Option(LongLeft), Putter),
    UserGolfInput(Distance(2), 3, OnTheGreen, Option(MiddleLeft), Putter),
    UserGolfInput(Distance(2), 4, OnTheGreen, Option(MiddleLeft), Putter),
    UserGolfInput(Distance(2), 5, OnTheGreen, Option(MiddleLeft), Putter)
  )

  val parThreeExampleGoodGolfer: List[UserGolfInput] = List(
    UserGolfInput(Distance(205), 1, TeeBox, Option(MiddleLeft), FourIron),
    UserGolfInput(Distance(10), 2, OnTheGreen, Option(LongLeft), Putter)
  )

  val parFourExample: List[UserGolfInput] = List(
    UserGolfInput(Distance(430), 1, TeeBox, Option(MiddleLeft), Driver),
    UserGolfInput(Distance(160), 2, Fairway, Option(MiddleLeft), FiveIron),
    UserGolfInput(Distance(10), 3, OnTheGreen, Option(LongLeft), Putter),
    UserGolfInput(Distance(2), 4, OnTheGreen, Option(MiddleLeft), Putter)
  )

  val parFourExampleHoleInOne: List[UserGolfInput] = List(
    UserGolfInput(Distance(430), 1, TeeBox, Option(MiddleLeft), Driver)
  )

  val parFiveExample: List[UserGolfInput] = List(
    UserGolfInput(Distance(559), 1, TeeBox, Option(MiddleLeft), Driver),
    UserGolfInput(Distance(320), 2, Fairway, Option(MiddleLeft), FiveWood),
    UserGolfInput(Distance(160), 3, Fairway, Option(MiddleLeft), FourIron),
    UserGolfInput(Distance(10), 4, OnTheGreen, Option(LongLeft), Putter),
    UserGolfInput(Distance(2), 5, OnTheGreen, Option(MiddleLeft), Putter)
  )

  val parFiveExampleBigDrive: List[UserGolfInput] = List(
    UserGolfInput(Distance(559), 1, TeeBox, Option(MiddleLeft), Driver),
    UserGolfInput(Distance(200), 2, Fairway, Option(MiddleLeft), FiveWood),
    UserGolfInput(Distance(160), 3, Fairway, Option(MiddleLeft), FourIron),
    UserGolfInput(Distance(10), 4, OnTheGreen, Option(LongLeft), Putter),
    UserGolfInput(Distance(2), 5, OnTheGreen, Option(MiddleLeft), Putter)
  )

}
