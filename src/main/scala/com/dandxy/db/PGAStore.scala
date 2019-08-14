package com.dandxy.db

import cats.effect.Bracket
import com.dandxy.golf.entity.PGAStatistics
import com.dandxy.golf.input.Distance
import com.dandxy.golf.pga.Statistic.PGAStatistic
import doobie.implicits._
import doobie.util.transactor.Transactor

import scala.language.higherKinds

trait PGAStore[F[_]] {
  def getStatistic(distance: Distance, stat: PGAStatistics): F[Option[PGAStatistic]]
}

class PGAPostgresQueryTool[F[_]](xa: Transactor[F])(implicit F: Bracket[F, Throwable]) extends PGAStore[F] {

  import com.dandxy.db.sql.PGAQueryToolSQL._

  def getStatistic(distance: Distance, stat: PGAStatistics): F[Option[PGAStatistic]] =
    findStatistic(distance, stat).transact(xa)

}
