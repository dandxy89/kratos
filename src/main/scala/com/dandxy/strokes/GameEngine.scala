//package com.dandxy.strokes
//
//import cats.Monad
//import cats.implicits._
//import com.dandxy.model.golf.entity.Location
//import com.dandxy.model.golf.input.{ Distance, HoleResult }
//import com.dandxy.model.golf.input.GolfInput.{ UserCourseInput, UserHoleInput }
//import com.dandxy.model.golf.pga.Statistic.PGAStatistic
//
//import scala.language.higherKinds
//
//object GameEngine {
//
//  def handle[F[_]: Monad](dbOp: Location => Distance => F[PGAStatistic])(in: UserCourseInput) = for {
//    allStrokesGained <- calculateAllStrokesGained(dbOp)(in.holeInput)
//    _ <- allStrokesGained
//  } yield ()
//
//  def calculateAllStrokesGained[F[_]: Monad](dbOp: Location => Distance => F[PGAStatistic])
//                                            (in: List[UserHoleInput]): F[List[HoleResult]] =
//    in.map(holeResult => StrokesGainedCalculator.calculate(dbOp)(holeResult.golfInput, holeResult.par)).sequence
//
//  // TODOs
//  // Best X shots
//  // Worst X shots
//  // Greens in regulation
//
//  // Shot orientation per club
//  // Average distance per club
//  // Best performing club
//  // Worst performing club
//
//  // displays without worst X shots
//
//  // Handling penalties
//  //  View by 3-6-9 hole splits
//  //  Scorecard view
//  //  Shot on handicap?
//
//}
