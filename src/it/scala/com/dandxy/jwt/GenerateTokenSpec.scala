package com.dandxy.jwt

import com.dandxy.model.player.PlayerId
import org.scalatest.{ FlatSpec, Matchers }

class GenerateTokenSpec extends FlatSpec with Matchers {

  behavior of "GenerateTokenSpec"

  it should "prepareToken" in {
    val token = GenerateToken.prepareToken(2, "soSecret")(PlayerId(123))
    assert(token.length > 65)
  }
}
