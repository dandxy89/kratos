package com.dandxy.db

import cats.effect.concurrent.Ref
import cats.effect.{ ContextShift, IO, Timer }
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import doobie.implicits._
import doobie.util.transactor.Transactor
import fs2.Stream

import scala.concurrent.duration._

object HealthCheck extends LazyLogging {

  sealed abstract class Status(val entryName: String)
  case object OK      extends Status("Ok")
  case object Warning extends Status("Warning")

  def queryStatus(db: Transactor[IO]): IO[Status] =
    sql"select 1"
      .query[Int]
      .unique
      .transact(db)
      .attempt
      .map {
        case Right(_) => OK
        case Left(e) =>
          logger.error(s"Lost connection to the database", e)
          Warning
      }

  def databaseStatusPoll(db: Transactor[IO])(implicit ec: ContextShift[IO], t: Timer[IO]): IO[Ref[IO, Status]] =
    for {
      status <- Ref.of[IO, Status](OK)
      _ <- Stream
        .repeatEval[IO, Unit](IO.sleep(10.seconds) *> queryStatus(db) >>= status.set)
        .compile
        .drain
        .start

    } yield status
}
