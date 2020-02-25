package com.dandxy.db.sql

import com.dandxy.auth.Password
import com.dandxy.golf.entity._
import com.dandxy.golf.input.Temperature.Celsius
import com.dandxy.golf.input._
import com.dandxy.model.player.PlayerId
import com.dandxy.model.user.Identifier.{GameId, Hole}
import com.dandxy.model.user.{ShotSerialId, UserEmail}
import doobie.Meta

object PostgresInstances {
  implicit val m1: Meta[Password]            = Meta[String].imap(Password)(_.value)
  implicit val m2: Meta[GolfClub]            = Meta[Int].imap(GolfClub.fromInt)(_.dbIndex)
  implicit val m3: Meta[Location]            = Meta[Int].imap(Location.fromId)(_.locationId)
  implicit val m4: Meta[Manufacturer]        = Meta[Int].imap(Manufacturer.fromInt)(_.id)
  implicit val m5: Meta[Orientation]         = Meta[Int].imap(Orientation.fromId)(_.code)
  implicit val m6: Meta[Par]                 = Meta[Int].imap(Par.fromInt)(_.strokes)
  implicit val m7: Meta[Score]               = Meta[Int].imap(Score.fromId)(_.s)
  implicit val m8: Meta[Distance]            = Meta[Int].timap(v => Distance(v.toDouble))(v => v.value.toInt)
  implicit val m9: Meta[DistanceMeasurement] = Meta[Int].imap(DistanceMeasurement.fromId)(_.id)
  implicit val m10: Meta[Handicap]           = Meta[Double].imap(Handicap(_))(_.value)
  implicit val m11: Meta[Points]             = Meta[Int].imap(Points(_))(_.value)
  implicit val m12: Meta[ShotHeight]         = Meta[Int].imap(ShotHeight.fromId)(_.id)
  implicit val m13: Meta[ShotShape]          = Meta[Int].imap(ShotShape.fromId)(_.id)
  implicit val m14: Meta[Strokes]            = Meta[Double].imap(Strokes(_))(_.value)
  implicit val m15: Meta[Temperature]        = Meta[Double].imap[Temperature](v => Celsius(v))(_.toCelsius)
  implicit val m16: Meta[WindSpeed]          = Meta[Int].imap(WindSpeed.fromId)(_.id)
  implicit val m17: Meta[PlayerId]           = Meta[Int].imap(PlayerId(_))(_.id)
  implicit val m18: Meta[GameId]             = Meta[Int].imap(GameId(_))(_.id)
  implicit val m19: Meta[Hole]               = Meta[Int].imap(Hole(_))(_.id)
  implicit val m20: Meta[ShotSerialId]       = Meta[Int].imap(ShotSerialId(_))(_.id)
  implicit val m21: Meta[UserEmail]          = Meta[String].imap(UserEmail(_))(_.email)
}
