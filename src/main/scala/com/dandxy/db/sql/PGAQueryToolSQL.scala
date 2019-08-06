package com.dandxy.db.sql

import com.dandxy.model.golf.entity.PGAStatistics
import com.dandxy.model.golf.input.Distance
import com.dandxy.model.golf.pga.Statistic.PGAStatistic
import doobie.free.connection.ConnectionIO
import doobie.implicits._

object PGAQueryToolSQL {

  private[db] def findStatistic(distance: Distance, stat: PGAStatistics): ConnectionIO[Option[PGAStatistic]] =
    (fr"SELECT distance, strokes FROM " ++ stat.tableName ++ fr" WHERE distance = $distance").stripMargin
      .query[PGAStatistic]
      .option

  // TODO: def findProbability(distance: Distance, stat: PGAStatistics): ConnectionIO[Option]

}
