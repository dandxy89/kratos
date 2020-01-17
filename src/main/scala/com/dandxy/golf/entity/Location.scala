package com.dandxy.golf.entity

import cats.Eq
import com.dandxy.db.sql.TableName
import com.dandxy.golf.entity.Penalty._
import doobie.implicits._
import doobie.util.fragment.Fragment
import io.circe.syntax._
import io.circe.{ Decoder, Encoder }

sealed trait PGAStatistics {
  val tableName: Fragment
}

sealed trait Location extends PGAStatistics {
  def name: String
  def locationId: Int
  def shots: Int
}

sealed trait Penalty extends Location {
  def description: String
  def shots: Int
}

object Penalty {

  case object Hazard extends Penalty {
    val description: String = "Water hazards of all types"
    val shots: Int          = 1
    val name: String        = "Penalty - hazard"
    val locationId: Int     = 7
    val tableName: Fragment = fr""
  }

  case object OutOfBounds extends Penalty {
    val description: String = "Out of bounds"
    val shots: Int          = 1
    val name: String        = "Penalty - out of bounds"
    val locationId: Int     = 8
    val tableName: Fragment = fr""
  }

  case object LostBall extends Penalty {
    val description: String = "Lost ball"
    val shots: Int          = 1
    val name: String        = "Penalty - lost ball"
    val locationId: Int     = 9
    val tableName: Fragment = fr""
  }

  case object UnplayableLie extends Penalty {
    val description: String = "Unplayable lie"
    val shots: Int          = 1
    val name: String        = "Penalty - unplayable lie"
    val locationId: Int     = 10
    val tableName: Fragment = fr""
  }

  case object OneShotPenalty extends Penalty {
    val description: String = "Technical: One shot penalty"
    val shots: Int          = 1
    val name: String        = "Penalty - one shot penalty"
    val locationId: Int     = 11
    val tableName: Fragment = fr""
  }

  case object TwoShotPenalty extends Penalty {
    val description: String = "Technical: Two shot penalty"
    val shots: Int          = 2
    val name: String        = "Penalty - two shot penalty"
    val locationId: Int     = 12
    val tableName: Fragment = fr""
  }
}

object Location {

  case object TeeBox extends Location with PGAStatistics {
    val name: String        = "Tee"
    val tableName: Fragment = TableName.TeeLookUp.name
    val locationId: Int     = 1
    val shots: Int          = 1
  }

  case object Fairway extends Location with PGAStatistics {
    val name: String        = "Fairway"
    val tableName: Fragment = TableName.FairwayLookup.name
    val locationId: Int     = 2
    val shots: Int          = 1
  }

  case object Rough extends Location with PGAStatistics {
    val name: String        = "Rough"
    val tableName: Fragment = TableName.RoughLookup.name
    val locationId: Int     = 3
    val shots: Int          = 1
  }

  case object Bunker extends Location with PGAStatistics {
    val name: String        = "Bunker"
    val tableName: Fragment = TableName.SandLookup.name
    val locationId: Int     = 4
    val shots: Int          = 1
  }

  case object Recovery extends Location with PGAStatistics {
    val name: String        = "Recovery shot"
    val tableName: Fragment = TableName.RecoveryLookup.name
    val locationId: Int     = 5
    val shots: Int          = 1
  }

  case object OnTheGreen extends Location with PGAStatistics {
    val name: String        = "Green"
    val tableName: Fragment = TableName.GreenLookup.name
    val locationId: Int     = 6
    val shots: Int          = 1
  }

  val approachLies: Set[Location] = Set(Bunker, Rough, Fairway, Recovery, TeeBox)

  implicit val locationEq: Eq[Location] = Eq.instance((a, b) => a.name == b.name)

  def fromId(value: Int): Location = value match {
    case 1  => TeeBox
    case 2  => Fairway
    case 3  => Rough
    case 4  => Bunker
    case 5  => Recovery
    case 6  => OnTheGreen
    case 7  => Hazard
    case 8  => OutOfBounds
    case 9  => LostBall
    case 10 => UnplayableLie
    case 11 => OneShotPenalty
    case _  => TwoShotPenalty
  }

  implicit val en: Encoder[Location] = Encoder.instance(_.locationId.asJson)
  implicit val de: Decoder[Location] = Decoder.instance(_.as[Int].map(fromId))
}
