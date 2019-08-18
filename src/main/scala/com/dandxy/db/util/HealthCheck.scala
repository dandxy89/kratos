package com.dandxy.db.util

import cats.effect.concurrent.Ref
import cats.effect.{ Bracket, Concurrent, Timer }
import cats.implicits._
import com.typesafe.scalalogging.StrictLogging
import doobie.implicits._
import doobie.util.transactor.Transactor
import fs2.Stream

import scala.concurrent.duration._
import scala.language.higherKinds

object HealthCheck extends StrictLogging {

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
        case Right(_) =>
          logger.debug("Database connection is healthy")
          OK
        case Left(e) =>
          logger.warn(s"Lost connection to the database", e)
          Warning
      }

  def raceQuery[F[_]](db: Transactor[F], ref: Ref[F, Status])(implicit t: Timer[F], F: Concurrent[F]): F[Unit] =
    F.race(
        queryStatus(db) >>= ref.set,
        t.sleep(3.seconds) *> {
          logger.warn("The database failed to communicate within 3 seconds")
          ref.set(Warning)
        }
      )
      .void

  def databaseStatusPoll[F[_]](db: Transactor[F])(implicit t: Timer[F], F: Concurrent[F]): F[Ref[F, Status]] =
    for {
      status <- Ref.of[F, Status](OK)
      _ <- F.start(
        Stream
          .repeatEval[F, Unit](t.sleep(10.seconds) *> raceQuery(db, status))
          .compile
          .drain
          .foreverM
          .void
      )
    } yield status
}
