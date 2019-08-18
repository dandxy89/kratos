package com.dandxy.model.http

object syntax {
  implicit class ResponseOps[A](val value: A)(implicit isRequest: IsResponse[A]) {
    def status: Int                 = isRequest.status(value)
    def contentLength: Option[Long] = isRequest.contentLength(value)
  }

  implicit class RequestOps[A](val value: A)(implicit isRequest: IsRequest[A]) {
    def headerByName(name: String): Option[String] = isRequest.headerByName(value, name)
    def method: String                             = isRequest.method(value)
    def path: String                               = isRequest.path(value)
    def params: Map[String, String]                = isRequest.params(value)
  }
}
