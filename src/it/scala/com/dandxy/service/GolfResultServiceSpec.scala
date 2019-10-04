package com.dandxy.service

import cats.effect._
import com.dandxy.golf.entity.Score.{ Aggregate, ParredHole }
import com.dandxy.golf.input.{ Points, Strokes }
import com.dandxy.model.user.Identifier.{ GameId, Hole }
import com.dandxy.service.GolfResultService._
import com.dandxy.strokes.GolfResult
import com.dandxy.testData.MockRouteTestData

import scala.concurrent.ExecutionContext

class GolfResultServiceSpec extends MockRouteTestData {

  behavior of "GolfResultServiceSpec"

  implicit private val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  it should "processHoleResult" in {
    val res1 = processHoleResult(mockStore, mockStat)(GameId(123), Option(Hole(1)), true).unsafeRunSync()
    val res2 = processHoleResult(mockStore, mockStat)(GameId(124), Option(Hole(1)), false).unsafeRunSync()

    res1 match {
      case None => fail()
      case Some(value) =>
        value shouldBe GolfResult(
          GameId(1),
          ParredHole,
          Some(Strokes(-1.9)),
          Some(Strokes(0.0)),
          Some(Strokes(-1.0)),
          None,
          Some(Strokes(-0.9)),
          Points(3)
        )
    }

    res2 match {
      case None        => fail()
      case Some(value) => value shouldBe GolfResult(GameId(124), Aggregate(3), None, None, None, None, None, Points(2))
    }
  }

  it should "processGolfResult" in {
    val res1 = processGolfResult(mockStore, mockStat)(GameId(125), true).unsafeRunSync()
    val res2 = processGolfResult(mockStore, mockStat)(GameId(126), false).unsafeRunSync()

    res1 shouldBe GolfResult(
      GameId(125),
      Aggregate(0),
      Some(Strokes(-8.7)),
      Some(Strokes(-2.0)),
      Some(Strokes(-4.0)),
      None,
      Some(Strokes(-2.7)),
      Points(9)
    )

    res2 shouldBe GolfResult(GameId(126), Aggregate(3), None, None, None, None, None, Points(2))
  }
}
