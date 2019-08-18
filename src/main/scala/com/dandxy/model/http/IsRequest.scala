package com.dandxy.model.http

trait IsRequest[A] {
  def headerByName(value: A, name: String): Option[String]
  def method(value: A): String
  def path(value: A): String
  def params(value: A): Map[String, String]
}

object IsRequest {
  def apply[A: IsRequest]: IsRequest[A] = implicitly[IsRequest[A]]
}
