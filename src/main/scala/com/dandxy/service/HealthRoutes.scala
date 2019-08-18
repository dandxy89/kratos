package com.dandxy.service

import cats.effect.Sync
import cats.effect.concurrent.Ref
import cats.implicits._
import com.dandxy.db.util.DatabaseStatus
import com.dandxy.db.util.HealthCheck.Status
import com.dandxy.middleware.implementation.http4s.defaults._
import com.dandxy.middleware.implementation.http4s.syntax._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

import scala.language.higherKinds

class HealthRoutes[F[_]](dbStatus: Ref[F, Status])(implicit F: Sync[F]) extends Http4sDsl[F] {

  val healthService: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ GET -> Root / "ping" => "Pong".negotiate[F](req)

    case req @ GET -> Root / "db" / "status" =>
      dbStatus.get.map(DatabaseStatus(_)).negotiate(req)
  }
}

object HealthRoutes {
  def apply[F[_]: Sync](dbStatus: Ref[F, Status]) = new HealthRoutes[F](dbStatus)
}
