package com.dandxy.db

import com.dandxy.model.golf.entity.PGAStatistics
import com.dandxy.model.golf.input.Distance
import com.dandxy.model.golf.pga.Statistic.PGAStatistic
import doobie.free.connection.ConnectionIO

object PGAQueryTool {

  import com.dandxy.db.sql.PGAQueryToolSQL._

  def getStatistic(distance: Distance, stat: PGAStatistics): ConnectionIO[Option[PGAStatistic]] =
    findStatistic(distance, stat)

  // TODO: Need to add more data
  // TODO: Finish queries
  //  def getProbability(distance: Distance, stat: PGAStatistics) = stat match {
  //    case Location.Fairway    => 1
  //    case Location.Rough      => 1
  //    case Location.Bunker     => 1
  //    case Location.Recovery   => 1
  //    case Location.OnTheGreen => 1
  //    case _                   => 1
  //  }

}
