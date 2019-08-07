package com.dandxy.auth

import java.nio.charset.Charset
import java.security.SecureRandom
import java.util.Base64

import com.dandxy.model.user.Password
import de.mkammerer.argon2.{ Argon2Factory, Argon2Helper }
import profig.Profig

object PasswordAuth {

  private lazy val argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)

  lazy val maxMillis: Long = Profig("scalapass.maxMillis").as[Long](1000L)

  lazy val memory: Int = Profig("scalapass.memory").as[Int](65536)

  lazy val parallelism: Int = Profig("scalapass.parallelism").as[Int](8)

  lazy val determineIdealIterations: Boolean = Profig("scalapass.determineIdealIterations")
    .as[String]("false")
    .toBoolean

  lazy val defaultIterations: Int = Profig("scalapass.defaultIterations").as[Int](50)

  lazy val iterations: Int = Profig("scalapass.iterations").as[Int] {
    if (determineIdealIterations) Argon2Helper.findIterations(argon2, maxMillis, memory, parallelism)
    else defaultIterations
  }

  lazy val saltBytes: Int = Profig("scalapass.saltBytes").as[Int](64)

  lazy val saltStrong: Boolean = Profig("scalapass.saltStrong").as[String]("false").toBoolean

  lazy val saltWeakAlgorithm: String = Profig("scalapass.saltWeakAlgorithm").as[String]("SHA1PRNG")

  lazy val charset: Charset = Charset.forName(Profig("scalapass.charset").as[String]("UTF-8"))

  def generateSalt(bytes: Int = saltBytes, strong: Boolean = saltStrong): Salt = {
    val secureRandom = if (strong) SecureRandom.getInstanceStrong else SecureRandom.getInstance(saltWeakAlgorithm)
    val salt         = new Array[Byte](bytes)
    secureRandom.nextBytes(salt)
    val base64 = Base64.getEncoder.encodeToString(salt)
    new Salt(base64)
  }

  private def salted(password: String, salt: Option[Salt]): String = salt.map(s => s"${s.base64}$password").getOrElse(password)

  def hash(
    password: Password,
    salt: Option[Salt] = None,
    iterations: Int = this.iterations,
    memory: Int = this.memory,
    parallelism: Int = this.parallelism
  ): String =
    argon2.hash(iterations, memory, parallelism, salted(password.value, salt))

  def verify(attemptedPassword: Password, hash: String, salt: Option[Salt] = None): Boolean =
    argon2.verify(hash, salted(attemptedPassword.value, salt))

}
