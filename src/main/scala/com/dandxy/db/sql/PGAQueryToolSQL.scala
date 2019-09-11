package com.dandxy.db.sql

import com.dandxy.golf.entity.Location
import com.dandxy.golf.input.Distance
import com.dandxy.golf.pga.Statistic.PGAStatistic
import doobie.free.connection.ConnectionIO
import doobie.implicits._

object PGAQueryToolSQL {

  private[db] def findStatistic(distance: Distance, stat: Location): ConnectionIO[Option[PGAStatistic]] =
    (fr"SELECT distance, strokes FROM " ++ stat.tableName ++ fr" WHERE distance = $distance")
      .stripMargin
      .query[PGAStatistic]
      .option

  // TODO: def findProbability(distance: Distance, stat: PGAStatistics): ConnectionIO[Option]

}
