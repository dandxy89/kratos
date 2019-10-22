package com.dandxy.db

import cats.effect.Bracket
import com.dandxy.db.sql.MetricsSQL._
import com.dandxy.golf.input.{ Distance, Strokes }
import com.dandxy.model.stats._
import com.dandxy.model.user.Identifier.GameId
import doobie.implicits._
import doobie.util.transactor.Transactor

import scala.language.higherKinds

trait MetricsStore[F[_]] {
  def strokesGainedByClub(gameId: GameId): F[List[StokesGainedByClub]]
  def bestXShots(gameId: GameId, n: Int): F[List[StrokesGainedResults]]
  def worstXShots(gameId: GameId, n: Int): F[List[StrokesGainedResults]]
  def greenInRegulation(gameId: GameId): F[Option[InRegulation]]
  def fairwaysHit(gameId: GameId): F[Option[InRegulation]]
  def averageDistanceByClub(gameId: GameId): F[List[ClubDistance]]
  def strokesGainedWithoutXShots(gameId: GameId, n: Int): F[Option[Strokes]]
  def puttsPerRound(gameId: GameId): F[Option[Strokes]]
  def penaltiesPerRound(gameId: GameId): F[Option[Strokes]]
  def averageDriveLength(gameId: GameId): F[List[AverageDriveDistance]]
  def holedPuttDistance(gameId: GameId): F[Option[Distance]]
  def puttsToHoleDistance(gameId: GameId, size: Distance, max: Distance): F[List[PuttsByDistanceToHole]]
  def standardScores(gameId: GameId): F[List[StandardScores]]
  // def bestPerformingClub(gameId: GameId): F[GolfClub]
  // def worstPerformingClub(gameId: GameId): F[GolfClub]
}

class MetricsStoreInterpreter[F[_]: Bracket[?[_], Throwable], A](xa: Transactor[F]) extends MetricsStore[F] {

  def strokesGainedByClub(gameId: GameId): F[List[StokesGainedByClub]] =
    getStrokesGainedByClub(gameId).transact(xa)

  def bestXShots(gameId: GameId, n: Int): F[List[StrokesGainedResults]] =
    getXStrokesGainedShots(gameId, n, best = true).transact(xa)

  def worstXShots(gameId: GameId, n: Int): F[List[StrokesGainedResults]] =
    getXStrokesGainedShots(gameId, n, best = false).transact(xa)

  def greenInRegulation(gameId: GameId): F[Option[InRegulation]] =
    getGameGreensInRegulation(gameId).transact(xa)

  def fairwaysHit(gameId: GameId): F[Option[InRegulation]] =
    getFairwaysInRegulation(gameId).transact(xa)

  def averageDistanceByClub(gameId: GameId): F[List[ClubDistance]] =
    getAverageDistanceByClub(gameId).transact(xa)

  def strokesGainedWithoutXShots(gameId: GameId, n: Int): F[Option[Strokes]] =
    getStrokesGainedWithoutNShots(gameId, n).option.transact(xa)

  def puttsPerRound(gameId: GameId): F[Option[Strokes]] =
    getPuttsPerRound(gameId).transact(xa)

  def penaltiesPerRound(gameId: GameId): F[Option[Strokes]] =
    getPenaltiesPerRound(gameId).transact(xa)

  def averageDriveLength(gameId: GameId): F[List[AverageDriveDistance]] =
    getAverageDriveDistance(gameId).transact(xa)

  def holedPuttDistance(gameId: GameId): F[Option[Distance]] =
    getHoledPuttDistance(gameId).option.transact(xa)

  def puttsToHoleDistance(gameId: GameId, size: Distance, max: Distance): F[List[PuttsByDistanceToHole]] =
    getPuttsToHoleFromDistance(gameId, size, max).transact(xa)

  def standardScores(gameId: GameId): F[List[StandardScores]] =
    getStandardScores(gameId).transact(xa)

}

object MetricsStoreInterpreter {

  def apply[F[_]: Bracket[?[_], Throwable], A](xa: Transactor[F]): MetricsStoreInterpreter[F, A] =
    new MetricsStoreInterpreter[F, A](xa: Transactor[F])

}
