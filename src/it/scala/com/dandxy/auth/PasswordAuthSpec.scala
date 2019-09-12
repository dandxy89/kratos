package com.dandxy.auth

import com.dandxy.model.user.Password
import org.scalatest.{ FlatSpec, Matchers }
import com.dandxy.auth.PasswordAuth._

class PasswordAuthSpec extends FlatSpec with Matchers {

  behavior of "PasswordAuthSpec"

  val testSalt: Salt           = Salt("testingSalt")
  val testPassword             = "testPassword"
  val testHashPassword: String = "$argon2id$v=19$m=65536,t=50,p=8$F7G2ZwXteYVMD/Xo5TPZ8A$8Ts3ZLAK67ED5Kb1ocT3iGogZd68s74lhanhUwVCVF0"

  it should "verify" in {
    assert(verifyPassword(Password(testPassword), testHashPassword, Some(testSalt)))
  }

  it should "hash" in {
    assert(hashPassword(Password(testPassword), salt = Option(testSalt)) != testHashPassword)
  }
}
