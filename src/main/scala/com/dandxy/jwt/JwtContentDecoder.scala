package com.dandxy.jwt

trait JwtContentDecoder[A] {
  def decode(content: String): Either[String, A]
}
