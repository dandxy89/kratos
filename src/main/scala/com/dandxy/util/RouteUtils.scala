package com.dandxy.util

import cats.MonadError
import cats.effect.Concurrent
import cats.syntax.all._
import com.dandxy.middleware.http4s.ToHttpResponse
import com.dandxy.middleware.http4s.content.syntax._
import com.dandxy.model.error.DomainError
import com.dandxy.util.Codecs._
import com.typesafe.scalalogging.StrictLogging
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.{Request, Response}

object RouteUtils extends StrictLogging {

  def runDbOp[F[_]: Concurrent, A](op: F[A], e: DomainError, r: Request[F])(implicit ME: MonadError[F, Throwable],
                                                                c: ToHttpResponse[F, A]): F[Response[F]] =
    ME.attempt(op).flatMap {
      case Right(value) => value.negotiate(r)
      case Left(error) =>
        logger.error(error.getMessage, error)
        e.negotiate(r)
    }
}
