package com.dandxy.service

import cats.effect.Concurrent
import cats.instances.list._
import cats.kernel.Semigroup
import cats.syntax.all._
import com.dandxy.db.UserStore
import com.dandxy.golf.entity.Location
import com.dandxy.golf.input.{ Distance, Handicap }
import com.dandxy.golf.pga.Statistic.PGAStatistic
import com.dandxy.model.user.Identifier.{ GameId, Hole }
import com.dandxy.strokes.GolfResult
import com.dandxy.strokes.StrokesGainedCalculator.calculateMany

import scala.language.higherKinds

object GolfResultHandler {

  private val defaultHandicap: Handicap = Handicap(0)

  def processGolfResult[F[_]](
    us: UserStore[F],
    getStatistic: (Distance, Location) => F[Option[PGAStatistic]]
  )(g: GameId, h: Option[Hole], overwrite: Boolean)(implicit F: Concurrent[F]): F[GolfResult] =
    us.getResultByIdentifier(g, h).flatMap {
      case Some(value) if !overwrite => value.pure[F]
      case _ =>
        (us.getByGameAndMaybeHole(g, h), us.getGameHandicap(g)).mapN { (in, hd) =>
          for {
            d <- calculateMany[F](getStatistic)(hd.getOrElse(defaultHandicap), in)
            a <- F.start(d.traverse(sg => us.addResultByIdentifier(sg.result, sg.shots.headOption.map(_.hole))))
            b <- F.start(d.traverse(sg => us.addPlayerShots(sg.shots)))
            r = d.map(_.result).foldLeft(GolfResult.empty(g))(Semigroup[GolfResult].combine)
            _ <- us.addResultByIdentifier(r, None)
            _ <- a.join
            _ <- b.join
          } yield r
        }.flatten
    }

  def processHoleResult[F[_]](
    us: UserStore[F],
    getStatistic: (Distance, Location) => F[Option[PGAStatistic]]
  )(g: GameId, h: Option[Hole], overwrite: Boolean)(implicit F: Concurrent[F]): F[Option[GolfResult]] =
    us.getResultByIdentifier(g, h).flatMap {
      case Some(value) if !overwrite => Option(value).pure[F]
      case _ =>
        (us.getByGameAndMaybeHole(g, h), us.getGameHandicap(g)).mapN { (in, hd) =>
          for {
            d <- calculateMany[F](getStatistic)(hd.getOrElse(defaultHandicap), in)
            a <- F.start(d.traverse(sg => us.addResultByIdentifier(sg.result, sg.shots.headOption.map(_.hole))))
            _ <- d.traverse(sg => us.addPlayerShots(sg.shots))
            _ <- a.join
          } yield d.map(_.result).headOption
        }.flatten
    }
}
