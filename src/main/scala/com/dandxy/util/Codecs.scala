package com.dandxy.util

import cats.Monad
import cats.data.OptionT
import cats.implicits._
import com.dandxy.middleware.ToResponse
import com.dandxy.middleware.http4s.ToHttpResponse
import com.dandxy.model.error.DomainError
import com.dandxy.model.error.DomainError.{ InvalidDataProvided, InvalidDistance, StatisticNotKnown }
import org.http4s.{ EntityEncoder, Response, Status }

import scala.language.higherKinds

object Codecs {

  implicit def errorResponse[F[_]: Monad](implicit ee: EntityEncoder[F, DomainError]): ToHttpResponse[F, DomainError] =
    ToResponse.instance { (_, value) =>
      val status = value match {
        case _: InvalidDistance | InvalidDataProvided => Status.BadRequest
        case _: StatisticNotKnown                     => Status.NotFound
      }

      Response[F](status).withEntity(value).pure[OptionT[F, ?]]
    }
}
