package com.dandxy.middleware.http4s.instances

import cats.Monad
import cats.implicits._
import cats.data.OptionT
import com.dandxy.middleware.ToResponse
import com.dandxy.middleware.http4s.ToHttpResponse
import org.http4s.{Response, Status}

trait ToResponseInstances {

  implicit def unitResponse[F[_]: Monad]: ToHttpResponse[F, Unit] = ToResponse.pure(Response(Status.NoContent))

  implicit def optionResponse[F[_]: Monad, T](implicit encoder: ToHttpResponse[F, T]): ToHttpResponse[F, Option[T]] =
    ToResponse.instance { (media, value) =>
      value.fold(Response[F](Status.NotFound).pure[OptionT[F, ?]])(encoder.run(media))
    }
}
