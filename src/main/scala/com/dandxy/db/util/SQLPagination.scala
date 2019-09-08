package com.dandxy.db.util

import doobie._
import doobie.implicits._
import doobie.util.pos.Pos

trait SQLPagination {

  def limit[A: Read](lim: Int)(q: Query0[A])(implicit pos: Pos): Query0[A] =
    (Fragment(q.sql, Nil, Some(pos)) ++ fr"LIMIT $lim").query

  def paginate[A: Read](lim: Int, offset: Int)(q: Query0[A])(implicit pos: Pos): Query0[A] =
    (Fragment(q.sql, Nil, Some(pos)) ++ fr"LIMIT $lim OFFSET $offset").query
}

object SQLPagination extends SQLPagination
