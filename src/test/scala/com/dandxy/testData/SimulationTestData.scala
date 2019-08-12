package com.dandxy.testData

import cats.effect.IO
import com.dandxy.model.golf.entity.GolfClub._
import com.dandxy.model.golf.entity.Location
import com.dandxy.model.golf.entity.Location.{ Fairway, OnTheGreen, TeeBox }
import com.dandxy.model.golf.entity.Orientation._
import com.dandxy.model.golf.entity.Par.{ ParFive, ParFour, ParThree }
import com.dandxy.model.golf.entity.Penalty.OutOfBounds
import com.dandxy.model.golf.entity.Score._
import com.dandxy.model.golf.input.GolfInput.UserShotInput
import com.dandxy.model.golf.input.{ Distance, GolfResult, Points, Strokes }
import com.dandxy.model.golf.pga.Statistic.PGAStatistic
import com.dandxy.model.user.Identifier
import com.dandxy.model.user.Identifier.{ GameId, Hole }

trait SimulationTestData {

  def runIO[A](io: IO[A]): A = io.unsafeRunSync()

  def asIdentifier(h: Hole): Identifier = h.asInstanceOf[Identifier]

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

  val parThreeExample: List[UserShotInput] = List(
    UserShotInput(GameId(1), Hole(1), 1, ParThree, Distance(210), TeeBox, FourIron, None, 1, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(1), 2, ParThree, Distance(10), OnTheGreen, Putter, None, 1, Option(LongLeft), None, None, None),
    UserShotInput(GameId(1), Hole(1), 3, ParThree, Distance(2), OnTheGreen, Putter, None, 1, Option(MiddleLeft), None, None, None)
  )

  val expectedParThreeExample = GolfResult(
    Hole(1),
    ParredHole(3),
    Some(Strokes(0.15000000000000002)),
    Some(Strokes(0.0)),
    Some(Strokes(0.524)),
    None,
    Some(Strokes(-0.374)),
    Points(3),
    List(
      UserShotInput(
        GameId(1),
        Hole(1),
        1,
        ParThree,
        Distance(210.0),
        TeeBox,
        FourIron,
        Some(Strokes(0.524)),
        1,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(1),
        2,
        ParThree,
        Distance(10.0),
        OnTheGreen,
        Putter,
        Some(Strokes(-0.383)),
        1,
        Some(LongLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(1),
        3,
        ParThree,
        Distance(2.0),
        OnTheGreen,
        Putter,
        Some(Strokes(0.009)),
        1,
        Some(MiddleLeft),
        None,
        None,
        None
      )
    )
  )

  val parThreeExampleBadGolfer: List[UserShotInput] = List(
    UserShotInput(GameId(1), Hole(2), 1, ParThree, Distance(210), TeeBox, FourIron, None, 2, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(2), 2, ParThree, Distance(10), OnTheGreen, Putter, None, 2, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(2), 3, ParThree, Distance(2), OnTheGreen, Putter, None, 2, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(2), 4, ParThree, Distance(2), OnTheGreen, Putter, None, 2, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(2), 5, ParThree, Distance(2), OnTheGreen, Putter, None, 2, Option(MiddleLeft), None, None, None)
  )

  val expectedParThreeExampleBadGolfer = GolfResult(
    Hole(2),
    DoubleBogey(5),
    Some(Strokes(-1.85)),
    Some(Strokes(0.0)),
    Some(Strokes(0.524)),
    None,
    Some(Strokes(-2.374)),
    Points(1),
    List(
      UserShotInput(
        GameId(1),
        Hole(2),
        1,
        ParThree,
        Distance(210.0),
        TeeBox,
        FourIron,
        Some(Strokes(0.524)),
        2,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(2),
        2,
        ParThree,
        Distance(10.0),
        OnTheGreen,
        Putter,
        Some(Strokes(-0.383)),
        2,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(2),
        3,
        ParThree,
        Distance(2.0),
        OnTheGreen,
        Putter,
        Some(Strokes(-1.0)),
        2,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(2),
        4,
        ParThree,
        Distance(2.0),
        OnTheGreen,
        Putter,
        Some(Strokes(-1.0)),
        2,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(2),
        5,
        ParThree,
        Distance(2.0),
        OnTheGreen,
        Putter,
        Some(Strokes(0.009)),
        2,
        Some(MiddleLeft),
        None,
        None,
        None
      )
    )
  )

  val parThreeExampleGoodGolfer: List[UserShotInput] = List(
    UserShotInput(GameId(1), Hole(3), 1, ParThree, Distance(210), TeeBox, FourIron, None, 3, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(3), 2, ParThree, Distance(10), OnTheGreen, Putter, None, 3, Option(MiddleLeft), None, None, None)
  )

  val expectedParThreeExampleGoodGolfer = GolfResult(
    Hole(3),
    Birdie(2),
    Some(Strokes(1.15)),
    Some(Strokes(0.0)),
    Some(Strokes(0.524)),
    None,
    Some(Strokes(0.626)),
    Points(4),
    List(
      UserShotInput(
        GameId(1),
        Hole(3),
        1,
        ParThree,
        Distance(210.0),
        TeeBox,
        FourIron,
        Some(Strokes(0.524)),
        3,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(3),
        2,
        ParThree,
        Distance(10.0),
        OnTheGreen,
        Putter,
        Some(Strokes(0.626)),
        3,
        Some(MiddleLeft),
        None,
        None,
        None
      )
    )
  )

  val parFourExample: List[UserShotInput] = List(
    UserShotInput(GameId(1), Hole(4), 1, ParFour, Distance(430), TeeBox, Driver, None, 4, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(4), 2, ParFour, Distance(160), Fairway, FiveIron, None, 4, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(4), 3, ParFour, Distance(10), OnTheGreen, Putter, None, 4, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(4), 4, ParFour, Distance(2), OnTheGreen, Putter, None, 4, Option(MiddleLeft), None, None, None)
  )

  val expectedParFourExample = GolfResult(
    Hole(4),
    ParredHole(4),
    Some(Strokes(0.07999999999999995)),
    Some(Strokes(0.1)),
    Some(Strokes(0.354)),
    None,
    Some(Strokes(-0.374)),
    Points(3),
    List(
      UserShotInput(
        GameId(1),
        Hole(4),
        1,
        ParFour,
        Distance(430.0),
        TeeBox,
        Driver,
        Some(Strokes(0.1)),
        4,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(4),
        2,
        ParFour,
        Distance(160.0),
        Fairway,
        FiveIron,
        Some(Strokes(0.354)),
        4,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(4),
        3,
        ParFour,
        Distance(10.0),
        OnTheGreen,
        Putter,
        Some(Strokes(-0.383)),
        4,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(4),
        4,
        ParFour,
        Distance(2.0),
        OnTheGreen,
        Putter,
        Some(Strokes(0.009)),
        4,
        Some(MiddleLeft),
        None,
        None,
        None
      )
    )
  )

  val parFourExampleHoleInOne: List[UserShotInput] = List(
    UserShotInput(GameId(1), Hole(5), 1, ParFour, Distance(430), TeeBox, Driver, None, 4, Option(MiddleLeft), None, None, None)
  )

  val expectedParFourExampleHoleInOne = GolfResult(
    Hole(5),
    HoleInOne(1),
    Some(Strokes(3.08)),
    Some(Strokes(3.08)),
    None,
    None,
    None,
    Points(6),
    List(
      UserShotInput(
        GameId(1),
        Hole(5),
        1,
        ParFour,
        Distance(430.0),
        TeeBox,
        Driver,
        Some(Strokes(3.08)),
        4,
        Some(MiddleLeft),
        None,
        None,
        None
      )
    )
  )

  val parFiveExample: List[UserShotInput] = List(
    UserShotInput(GameId(1), Hole(6), 1, ParFive, Distance(559), TeeBox, Driver, None, 6, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(6), 2, ParFive, Distance(320), Fairway, FiveWood, None, 6, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(6), 3, ParFive, Distance(160), Fairway, FourIron, None, 6, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(6), 4, ParFive, Distance(10), OnTheGreen, Putter, None, 6, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(6), 5, ParFive, Distance(2), OnTheGreen, Putter, None, 6, Option(MiddleLeft), None, None, None)
  )

  val expectedParFiveExample = GolfResult(
    Hole(6),
    ParredHole(5),
    Some(Strokes(-0.26)),
    Some(Strokes(-0.1)),
    Some(Strokes(0.21399999999999997)),
    None,
    Some(Strokes(-0.374)),
    Points(3),
    List(
      UserShotInput(
        GameId(1),
        Hole(6),
        1,
        ParFive,
        Distance(559.0),
        TeeBox,
        Driver,
        Some(Strokes(-0.1)),
        6,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(6),
        2,
        ParFive,
        Distance(320.0),
        Fairway,
        FiveWood,
        Some(Strokes(-0.14)),
        6,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(6),
        3,
        ParFive,
        Distance(160.0),
        Fairway,
        FourIron,
        Some(Strokes(0.354)),
        6,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(6),
        4,
        ParFive,
        Distance(10.0),
        OnTheGreen,
        Putter,
        Some(Strokes(-0.383)),
        6,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(6),
        5,
        ParFive,
        Distance(2.0),
        OnTheGreen,
        Putter,
        Some(Strokes(0.009)),
        6,
        Some(MiddleLeft),
        None,
        None,
        None
      )
    )
  )

  val parFiveExampleBigDrive: List[UserShotInput] = List(
    UserShotInput(GameId(1), Hole(7), 1, ParFive, Distance(559), TeeBox, Driver, None, 7, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(7), 1, ParFive, Distance(200), Fairway, FiveWood, None, 7, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(7), 1, ParFive, Distance(160), Fairway, FourIron, None, 7, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(7), 1, ParFive, Distance(10), OnTheGreen, Putter, None, 7, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(7), 1, ParFive, Distance(2), OnTheGreen, Putter, None, 7, Option(MiddleLeft), None, None, None)
  )

  val expectedParFiveExampleBigDrive = GolfResult(
    Hole(7),
    ParredHole(5),
    Some(Strokes(-0.26)),
    Some(Strokes(0.55)),
    Some(Strokes(-0.43600000000000005)),
    None,
    Some(Strokes(-0.374)),
    Points(3),
    List(
      UserShotInput(
        GameId(1),
        Hole(7),
        1,
        ParFive,
        Distance(559.0),
        TeeBox,
        Driver,
        Some(Strokes(0.55)),
        7,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(7),
        1,
        ParFive,
        Distance(200.0),
        Fairway,
        FiveWood,
        Some(Strokes(-0.79)),
        7,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(7),
        1,
        ParFive,
        Distance(160.0),
        Fairway,
        FourIron,
        Some(Strokes(0.354)),
        7,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(7),
        1,
        ParFive,
        Distance(10.0),
        OnTheGreen,
        Putter,
        Some(Strokes(-0.383)),
        7,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(7),
        1,
        ParFive,
        Distance(2.0),
        OnTheGreen,
        Putter,
        Some(Strokes(0.009)),
        7,
        Some(MiddleLeft),
        None,
        None,
        None
      )
    )
  )

  val parFiveExampleBigDriveOutOfBounds: List[UserShotInput] = List(
    UserShotInput(GameId(1), Hole(8), 1, ParFive, Distance(559), TeeBox, Driver, None, 8, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(8), 2, ParFive, Distance(559), OutOfBounds, Driver, None, 8, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(8), 3, ParFive, Distance(559), TeeBox, Driver, None, 8, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(8), 4, ParFive, Distance(559), Fairway, FiveWood, None, 8, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(8), 5, ParFive, Distance(559), Fairway, FourIron, None, 8, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(8), 6, ParFive, Distance(559), OnTheGreen, Putter, None, 8, Option(MiddleLeft), None, None, None),
    UserShotInput(GameId(1), Hole(8), 7, ParFive, Distance(559), OnTheGreen, Putter, None, 8, Option(MiddleLeft), None, None, None)
  )

  val expectedParFiveExampleBigDriveOutOfBounds = GolfResult(
    Hole(8),
    DoubleBogey(7),
    Some(Strokes(-1.2599999999999998)),
    Some(Strokes(-1.0)),
    Some(Strokes(-3.626)),
    None,
    Some(Strokes(-0.374)),
    Points(0),
    List(
      UserShotInput(
        GameId(1),
        Hole(8),
        1,
        ParFive,
        Distance(559.0),
        TeeBox,
        Driver,
        Some(Strokes(-1.0)),
        8,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(8),
        3,
        ParFive,
        Distance(559.0),
        TeeBox,
        Driver,
        Some(Strokes(3.74)),
        8,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(8),
        4,
        ParFive,
        Distance(559.0),
        Fairway,
        FiveWood,
        Some(Strokes(-1.0)),
        8,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(8),
        5,
        ParFive,
        Distance(559.0),
        Fairway,
        FourIron,
        Some(Strokes(-2.626)),
        8,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(8),
        6,
        ParFive,
        Distance(559.0),
        OnTheGreen,
        Putter,
        Some(Strokes(-1.0)),
        8,
        Some(MiddleLeft),
        None,
        None,
        None
      ),
      UserShotInput(
        GameId(1),
        Hole(8),
        7,
        ParFive,
        Distance(559.0),
        OnTheGreen,
        Putter,
        Some(Strokes(0.626)),
        8,
        Some(MiddleLeft),
        None,
        None,
        None
      )
    )
  )

  val pgaExample: List[UserShotInput] = List(
    UserShotInput(
      GameId(2),
      Hole(8),
      1,
      ParThree,
      Distance(446.0),
      TeeBox,
      Driver,
      Some(Strokes(4.100)),
      8,
      Some(MiddleLeft),
      None,
      None,
      None
    ),
    UserShotInput(
      GameId(2),
      Hole(8),
      2,
      ParThree,
      Distance(116.0),
      Fairway,
      FiveWood,
      Some(Strokes(2.825)),
      8,
      Some(MiddleLeft),
      None,
      None,
      None
    ),
    UserShotInput(
      GameId(2),
      Hole(8),
      3,
      ParThree,
      Distance(17.0),
      OnTheGreen,
      Putter,
      Some(Strokes(1.826)),
      8,
      Some(MiddleLeft),
      None,
      None,
      None
    )
  )

  val pgaExpectedResult: GolfResult =
    GolfResult(
      Hole(8),
      ParredHole(3),
      Some(Strokes(1.1)),
      Some(Strokes(0.0)),
      Some(Strokes(0.27)),
      None,
      Some(Strokes(0.83)),
      Points(2),
      List(
        UserShotInput(
          GameId(2),
          Hole(8),
          1,
          ParThree,
          Distance(446.0),
          TeeBox,
          Driver,
          Some(Strokes(0.275)),
          8,
          Some(MiddleLeft),
          None,
          None,
          None
        ),
        UserShotInput(
          GameId(2),
          Hole(8),
          2,
          ParThree,
          Distance(116.0),
          Fairway,
          FiveWood,
          Some(Strokes(-0.005)),
          8,
          Some(MiddleLeft),
          None,
          None,
          None
        ),
        UserShotInput(
          GameId(2),
          Hole(8),
          3,
          ParThree,
          Distance(17.0),
          OnTheGreen,
          Putter,
          Some(Strokes(0.83)),
          8,
          Some(MiddleLeft),
          None,
          None,
          None
        )
      )
    )
}
