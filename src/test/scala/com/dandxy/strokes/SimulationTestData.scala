package com.dandxy.strokes

import cats.effect.IO
import com.dandxy.model.golf.DistanceMetric.Feet
import com.dandxy.model.golf.GolfClub._
import com.dandxy.model.golf.Location.{Fairway, OnTheGreen, TeeBox}
import com.dandxy.model.golf.Orientation.{LongLeft, MiddleLeft}
import com.dandxy.model.golf.{Distance, Hole, Location, PGAStatistic, Par}
import com.dandxy.model.player.{UserGolfInput, UserInput}

trait SimulationTestData {

  def testUserInput(inputData: List[UserGolfInput], holePar: Par) =
    UserInput("testID123", Feet, Hole(1), holePar, inputData)

  val dbCalled: Location => Distance => IO[PGAStatistic] = stat =>
    distance =>
      stat match {
        case TeeBox =>
          distance match {
            case Distance(430.0) => IO.pure(PGAStatistic(Distance(430), 4.08))
            case Distance(160.0) => IO.pure(PGAStatistic(Distance(160), 2.99))
            case Distance(559.0) => IO.pure(PGAStatistic(Distance(560), 4.74))
            case Distance(310.0) => IO.pure(PGAStatistic(Distance(310), 3.75))
            case Distance(210.0) => IO.pure(PGAStatistic(Distance(210), 3.15))
            case _               => IO.pure(PGAStatistic(Distance(100), 0))
          }
        case OnTheGreen => IO.pure(PGAStatistic(Distance(10), 1.626))
        case _ =>
          distance match {
            case Distance(430.0) => IO.pure(PGAStatistic(Distance(430), 4.08))
            case Distance(320.0) => IO.pure(PGAStatistic(Distance(320), 3.84))
            case Distance(160.0) => IO.pure(PGAStatistic(Distance(160), 2.98))
            case Distance(200.0) => IO.pure(PGAStatistic(Distance(200), 3.19))
            case _               =>
              println("DEFAULT")
              IO.pure(PGAStatistic(Distance(100), 0))
          }
      }

  val parThreeExample: List[UserGolfInput] = List(
    UserGolfInput(Distance(205), 1, TeeBox, MiddleLeft, FourIron),
    UserGolfInput(Distance(10), 2, OnTheGreen, LongLeft, Putter),
    UserGolfInput(Distance(2), 3, OnTheGreen, MiddleLeft, Putter)
  )

  val parThreeExampleBadGolfer: List[UserGolfInput] = List(
    UserGolfInput(Distance(205), 1, TeeBox, MiddleLeft, FourIron),
    UserGolfInput(Distance(10), 2, OnTheGreen, LongLeft, Putter),
    UserGolfInput(Distance(2), 3, OnTheGreen, MiddleLeft, Putter),
    UserGolfInput(Distance(2), 3, OnTheGreen, MiddleLeft, Putter),
    UserGolfInput(Distance(2), 3, OnTheGreen, MiddleLeft, Putter)
  )

  val parThreeExampleGoodGolfer: List[UserGolfInput] = List(
    UserGolfInput(Distance(205), 1, TeeBox, MiddleLeft, FourIron),
    UserGolfInput(Distance(10), 2, OnTheGreen, LongLeft, Putter)
  )

  val parFourExample: List[UserGolfInput] = List(
    UserGolfInput(Distance(430), 1, TeeBox, MiddleLeft, Driver),
    UserGolfInput(Distance(160), 1, Fairway, MiddleLeft, FiveIron),
    UserGolfInput(Distance(10), 2, OnTheGreen, LongLeft, Putter),
    UserGolfInput(Distance(2), 3, OnTheGreen, MiddleLeft, Putter)
  )

  val parFourExampleHoleInOne: List[UserGolfInput] = List(
    UserGolfInput(Distance(430), 1, TeeBox, MiddleLeft, Driver)
  )

  val parFiveExample: List[UserGolfInput] = List(
    UserGolfInput(Distance(559), 1, TeeBox, MiddleLeft, Driver),
    UserGolfInput(Distance(320), 1, Fairway, MiddleLeft, FiveWood),
    UserGolfInput(Distance(160), 1, Fairway, MiddleLeft, FourIron),
    UserGolfInput(Distance(10), 2, OnTheGreen, LongLeft, Putter),
    UserGolfInput(Distance(2), 3, OnTheGreen, MiddleLeft, Putter)
  )

  val parFiveExampleBigDrive: List[UserGolfInput] = List(
    UserGolfInput(Distance(559), 1, TeeBox, MiddleLeft, Driver),
    UserGolfInput(Distance(200), 1, Fairway, MiddleLeft, FiveWood),
    UserGolfInput(Distance(160), 1, Fairway, MiddleLeft, FourIron),
    UserGolfInput(Distance(10), 2, OnTheGreen, LongLeft, Putter),
    UserGolfInput(Distance(2), 3, OnTheGreen, MiddleLeft, Putter)
  )

}
