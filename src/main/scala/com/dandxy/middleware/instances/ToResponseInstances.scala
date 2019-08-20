package com.dandxy.middleware.instances

import cats.Monad
import cats.data.OptionT
import cats.syntax.all._
import com.dandxy.middleware.ToResponse
import com.dandxy.middleware.http4s.ToHttpResponse
import org.http4s.{Response, Status}

import scala.language.higherKinds

trait ToResponseInstances {

  implicit def unitResponse[F[_]: Monad]: ToHttpResponse[F, Unit] = ToResponse.pure(Response(Status.NoContent))

  implicit def optionResponse[F[_]: Monad, T](implicit encoder: ToHttpResponse[F, T]): ToHttpResponse[F, Option[T]] =
    ToResponse.instance { (media, value) =>
      value.fold(Response[F](Status.NotFound).pure[OptionT[F, ?]])(encoder.run(media))
    }
}
