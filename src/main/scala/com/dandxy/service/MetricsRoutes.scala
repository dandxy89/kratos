package com.dandxy.service

import cats.effect.Concurrent
import com.dandxy.db.MetricsStore
import com.dandxy.jwt.{ Claims }
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.{ AuthedRoutes, HttpRoutes }

import scala.language.higherKinds

class MetricsRoutes[F[_]](ms: MetricsStore[F], middleware: AuthMiddleware[F, Claims])(implicit F: Concurrent[F]) extends Http4sDsl[F] {
  
  val xy = ms

  private val routes: AuthedRoutes[Claims, F] = AuthedRoutes.of[Claims, F] {
    case _ => Ok()
  }

  val metricsRoutes: HttpRoutes[F] = middleware(routes)

}

object MetricsRoutes {

  def apply[F[_]](ms: MetricsStore[F], middleware: AuthMiddleware[F, Claims])(implicit F: Concurrent[F]) = 
    new MetricsRoutes[F](ms, middleware)

}
