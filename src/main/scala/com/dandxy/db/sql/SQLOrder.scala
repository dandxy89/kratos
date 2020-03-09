package com.dandxy.db.sql

sealed trait SQLOrder {
  def direction: String
}

object SQLOrder {

  case object Ascending extends SQLOrder {
    val direction = "ASC"
  }

  case object Descending extends SQLOrder {
    val direction = "DESC"
  }

  def fromString(value: String): SQLOrder =
    value match {
      case "ASC"  => Ascending
      case "DESC" => Descending
    }
}
