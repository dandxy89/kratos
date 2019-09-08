package com.dandxy.golf.entity

import com.dandxy.golf.entity.GolfClub._
import org.scalatest.{ FlatSpec, Matchers }

class GolfClubSpec extends FlatSpec with Matchers {

  behavior of "GolfClubSpec"

  it should "fromInt" in {
    GolfClub.fromInt(1) shouldBe Driver
    GolfClub.fromInt(2) shouldBe ThreeWood
    GolfClub.fromInt(3) shouldBe FourWood
    GolfClub.fromInt(4) shouldBe FiveWood
    GolfClub.fromInt(5) shouldBe SevenWood
    GolfClub.fromInt(6) shouldBe NineWood
    GolfClub.fromInt(7) shouldBe OneHybrid
    GolfClub.fromInt(8) shouldBe TwoHybrid
    GolfClub.fromInt(9) shouldBe ThreeHybrid
    GolfClub.fromInt(10) shouldBe FourHybrid
    GolfClub.fromInt(11) shouldBe FiveHybrid
    GolfClub.fromInt(12) shouldBe OneIron
    GolfClub.fromInt(13) shouldBe TwoIron
    GolfClub.fromInt(14) shouldBe ThreeIron
    GolfClub.fromInt(15) shouldBe FourIron
    GolfClub.fromInt(16) shouldBe FiveIron
    GolfClub.fromInt(17) shouldBe SixIron
    GolfClub.fromInt(18) shouldBe SevenIron
    GolfClub.fromInt(19) shouldBe EightIron
    GolfClub.fromInt(20) shouldBe NineIron
    GolfClub.fromInt(21) shouldBe PitchingWedge
    GolfClub.fromInt(22) shouldBe GapWedge
    GolfClub.fromInt(23) shouldBe SandWedge
    GolfClub.fromInt(24) shouldBe LobWedge
    GolfClub.fromInt(25) shouldBe Putter
  }
}
