package com.dandxy.model.http

trait IsResponse[A] {
  def status(value: A): Int
  def contentLength(value: A): Option[Long]
}

object IsResponse {
  def apply[A: IsResponse]: IsResponse[A] = implicitly[IsResponse[A]]
}
