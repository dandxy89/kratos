package com.dandxy.service

import cats.effect.Sync
import cats.implicits._
import org.http4s.dsl.Http4sDsl
import org.http4s.{ HttpRoutes, Response, Status }

import scala.language.higherKinds

class HealthRoutes[F[_]](implicit F: Sync[F]) extends Http4sDsl[F] {

  val pingPongService: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "ping" => Response[F](Status.Ok).pure[F]
  }
}

object HealthRoutes {
  def apply[F[_]: Sync]() = new HealthRoutes[F]()
}
