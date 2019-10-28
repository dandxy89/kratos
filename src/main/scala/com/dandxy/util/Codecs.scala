package com.dandxy.util

import java.sql.Timestamp

import cats.Monad
import cats.data.OptionT
import cats.implicits._
import com.dandxy.middleware.ToResponse
import com.dandxy.middleware.http4s.ToHttpResponse
import com.dandxy.model.error.DomainError
import com.dandxy.model.error.DomainError._
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}
import org.http4s.{EntityEncoder, Response, Status}

import scala.language.higherKinds

object Codecs {

  implicit def errorResponse[F[_]: Monad](implicit ee: EntityEncoder[F, DomainError]): ToHttpResponse[F, DomainError] =
    ToResponse.instance { (_, value) =>
      val status = value match {
        case _: InvalidDistance | InvalidDataProvided | InvalidPlayerProvided | InvalidGameProvided => Status.BadRequest
        case _: StatisticNotKnown                                                                   => Status.NotFound
        case NoContentInDB                                                                          => Status.NoContent
      }

      Response[F](status).withEntity(value).pure[OptionT[F, ?]]
    }

  implicit val TimestampFormat: Encoder[Timestamp] with Decoder[Timestamp] = new Encoder[Timestamp] with Decoder[Timestamp] {
    override def apply(a: Timestamp): Json            = Encoder.encodeLong.apply(a.getTime)
    override def apply(c: HCursor): Result[Timestamp] = Decoder.decodeLong.map(s => new Timestamp(s)).apply(c)
  }
}
