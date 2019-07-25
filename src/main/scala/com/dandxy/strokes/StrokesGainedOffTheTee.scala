//package com.dandxy.strokes
//
//import cats.Monad
//import cats.implicits._
//import com.dandxy.model.golf.Location.TeeBox
//import com.dandxy.model.golf.Par.ParThree
//import com.dandxy.model.golf.{Distance, Location, PGAStatistic}
//import com.dandxy.model.player.{UserGolfInput, UserInput}
//
//import scala.language.higherKinds
//
//object StrokesGainedOffTheTee {
//
//  def isEligible(input: UserInput): Boolean = if (input.par == ParThree) true else false
//
//  private[this] def getShots(input: List[UserGolfInput]): List[UserGolfInput] = input.take(2)
//
//  def calculate(teeShot: PGAStatistic, nextShot: Option[PGAStatistic]): Double =
//    (teeShot.strokes - nextShot.map(_.strokes).getOrElse(teeShot.strokes)) - 1
//
//  def getStrokesGained[F[_]: Monad](dbCall: Location => Distance => F[PGAStatistic])(input: UserInput): F[Double] =
//    if (isEligible(input)) 0.0.pure[F]
//    else {
//      getShots(input.golfInput) match {
//        case Nil      => 0.0.pure[F]
//        case a :: Nil => dbCall(TeeBox)(a.distance).map(r => calculate(r, None))
//        case a :: b :: Nil =>
//          for {
//            t <- dbCall(TeeBox)(a.distance)
//            f <- dbCall(b.location)(b.distance)
//          } yield calculate(t, Option(f))
//      }
//    }
//}
