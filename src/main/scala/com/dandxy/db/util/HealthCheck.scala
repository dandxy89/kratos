package com.dandxy.db.util

import cats.effect.concurrent.Ref
import cats.effect.{ Bracket, Concurrent, Timer }
import cats.implicits._
import com.typesafe.scalalogging.LazyLogging
import doobie.implicits._
import doobie.util.transactor.Transactor
import fs2.Stream

import scala.concurrent.duration._
import scala.language.higherKinds

object HealthCheck extends LazyLogging {

  sealed abstract class Status(val entryName: String)
  case object OK      extends Status("Ok")
  case object Warning extends Status("Warning")

  def queryStatus[F[_]](db: Transactor[F])(implicit b: Bracket[F, Throwable]): F[Status] =
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

  def databaseStatusPoll[F[_]](db: Transactor[F])(implicit t: Timer[F], F: Concurrent[F]): F[Ref[F, Status]] =
    for {
      status <- Ref.of[F, Status](OK)
      _ <- F.start(
        Stream
          .repeatEval[F, Unit](t.sleep(1.seconds) *> F.race(queryStatus(db) >>= status.set, t.sleep(2.seconds) *> status.set(Warning)).void)
          .compile
          .drain
          .foreverM
          .void
      )
    } yield status
}
