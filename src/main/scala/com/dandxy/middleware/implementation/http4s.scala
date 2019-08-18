package com.dandxy.middleware.implementation

import cats.{Applicative, Monad}
import com.dandxy.middleware.ToResponse
import com.dandxy.middleware.model.Exported
import org.http4s.headers.`Content-Type`
import org.http4s.util.CaseInsensitiveString
import org.http4s.{Charset, EntityEncoder, Headers, MediaRange, MediaType, Request, Response, Status}

import scala.language.higherKinds

package object http4s {

  type ToHttpResponse[F[_], T] = ToResponse[F, List[MediaRange], Response[F], T]

  def fromEncoder[F[_], T](
                            status: T => Status,
                            permitted: MediaRange => Boolean = _ => true
                          )(implicit F: Applicative[F], ee: EntityEncoder[F, T]): ToHttpResponse[F, T] =
    ToResponse.instanceF { (media, value) =>
      if (media.exists(permitted))
        F.pure(Some(Response[F](status(value)).withEntity(value).withContentTypeOption(ee.contentType)))
      else F.pure(None)
    }

  object defaults {
    implicit def entityEncoderResponse[F[_] : Applicative, T](implicit ee: EntityEncoder[F, T]): Exported[ToHttpResponse[F, T]] =
      Exported(fromEncoder[F, T](_ => Status.Ok))

  }

  object syntax {

    private def parseRequestAcceptHeader[F[_], T](req: Request[F]): Option[Array[MediaRange]] =
      req.headers
        .get(CaseInsensitiveString("Accept"))
        .map(header => header.value.split(',').flatMap(range => MediaRange.parse(range.trim).toOption))

    private def unacceptable[F[_] : Monad]: Response[F] = {
      val error =
        """{"error":"unsupported_accept_type", "error_description":"The media ranges could not be satisfied"}"""
      Response[F](Status.NotAcceptable, headers = Headers.of(`Content-Type`(MediaType.application.json, Charset.`UTF-8`)))
        .withEntity(error)
    }

    implicit class ToResponseOps[T](val value: T) extends AnyVal {
      def negotiate[F[_]](req: Request[F], fallback: Option[F[Response[F]]] = None
                         )(implicit converter: ToHttpResponse[F, T], F: Monad[F]): F[Response[F]] = {
        val mr = parseRequestAcceptHeader(req).fold(List.empty[MediaRange])(_.toList)
        converter.run(mr)(value).getOrElseF(fallback.getOrElse(F.pure(unacceptable)))
      }
    }
  }
}
