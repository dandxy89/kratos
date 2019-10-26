package com.dandxy.db.sql

import doobie.util.Meta

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
            case "ASC" => Ascending
            case "DESC" => Descending
        }

    implicit val meta: Meta[SQLOrder]  = Meta[String].timap(v => fromString(v))(v => v.direction)

}

