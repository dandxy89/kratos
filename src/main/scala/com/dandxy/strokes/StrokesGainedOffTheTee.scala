package com.dandxy.strokes

import cats.Monad
import cats.implicits._
import com.dandxy.model.golf.Location.TeeBox
import com.dandxy.model.golf.Par.ParThree
import com.dandxy.model.golf.Statistic.PGAStatistic
import com.dandxy.model.golf.{Distance, Location}
import com.dandxy.model.player.GolfInput.{UserGolfInput, UserInput}

import scala.language.higherKinds

class StrokesGainedOffTheTee[F[_]: Monad](dbOp: Location => Distance => F[PGAStatistic]) {

  def isEligible(input: UserInput): Boolean = if (input.par == ParThree) true else false

  private[this] def getShots(input: List[UserGolfInput]): List[UserGolfInput] = input.take(2)

  def calculate(teeShot: PGAStatistic, nextShot: Option[PGAStatistic]): Double =
    (teeShot.strokes - nextShot.map(_.strokes).getOrElse(teeShot.strokes)) - 1

  def retrieveTwoShots(a: UserGolfInput, b: UserGolfInput): F[Double] =
    (dbOp(TeeBox)(a.distance), dbOp(b.location)(b.distance)).mapN {
      case (m9, m1) => calculate(m9, Option(m1))
    }

  def getStrokesGained(input: UserInput): F[Double] =
    if (isEligible(input)) 0.0.pure[F]
    else {
      getShots(input.golfInput) match {
        case a :: Nil      => dbOp(TeeBox)(a.distance).map(r => calculate(r, None))
        case a :: b :: Nil => retrieveTwoShots(a, b)
        case _             => 0.0.pure[F]
      }
    }
}
