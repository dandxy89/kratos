package com.dandxy.service

import cats.effect.Sync
import com.dandxy.middleware.implementation.http4s.defaults._
import com.dandxy.middleware.implementation.http4s.syntax._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

import scala.language.higherKinds

class HealthRoutes[F[_]](implicit F: Sync[F]) extends Http4sDsl[F] {

  val pingPongService: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ GET -> Root / "ping" => "Pong".negotiate[F](req)
  }
}

object HealthRoutes {
  def apply[F[_]: Sync]() = new HealthRoutes[F]()
}
