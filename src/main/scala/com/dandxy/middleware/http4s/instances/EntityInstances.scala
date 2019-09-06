package com.dandxy.middleware.http4s.instances

import com.dandxy.model.http.{IsRequest, IsResponse}
import org.http4s.util.CaseInsensitiveString
import org.http4s.{Request, Response}

trait EntityInstances {

  implicit def isRequest[F[_]]: IsRequest[Request[F]] = new IsRequest[Request[F]] {

    override def headerByName(value: Request[F], name: String): Option[String] =
      value.headers.get(CaseInsensitiveString(name)).map(_.value)

    override def method(value: Request[F]): String              = value.method.name
    override def path(value: Request[F]): String                = value.uri.path
    override def params(value: Request[F]): Map[String, String] = value.params
  }

  implicit def isResponse[F[_]]: IsResponse[Response[F]] = new IsResponse[Response[F]] {
    override def status(value: Response[F]): Int                 = value.status.code
    override def contentLength(value: Response[F]): Option[Long] = value.contentLength
  }
}
