package com.dandxy.db

import com.dandxy.model.golf.entity.PGAStatistics
import com.dandxy.model.golf.input.Distance
import com.dandxy.model.golf.pga.Statistic.PGAStatistic
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.Meta

object PGAQueryTool {

  implicit val metaTranslator: Meta[Distance] = Meta[Int].timap(v => Distance(v.toDouble))(v => v.value.toInt)

  def findStatistic(distance: Distance, stat: PGAStatistics): ConnectionIO[Option[PGAStatistic]] =
    (fr"SELECT distance, strokes FROM " ++ stat.dbLocation ++ fr" WHERE distance = $distance").stripMargin
      .query[PGAStatistic]
      .option
}
