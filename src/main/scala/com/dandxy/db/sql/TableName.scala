package com.dandxy.db.sql

import doobie.implicits._
import doobie.util.fragment.Fragment

sealed trait TableName {
  val name: Fragment
}

object TableName {

  case object PlayerShot extends TableName {
    val name: Fragment = fr"player.shot"
  }

  case object PlayerGame extends TableName {
    val name: Fragment = fr"player.game"
  }

  case object PlayerClubData extends TableName {
    val name: Fragment = fr"player.club_data"
  }

  case object UserSecurity extends TableName {
    val name: Fragment = fr"userSecurity.hashedpassword"
  }

  case object PlayerLookup extends TableName {
    val name: Fragment = fr"player.playerlookup"
  }

  case object TeeLookUp extends TableName {
    val name: Fragment = fr"pga.teelookup"
  }

  case object FairwayLookup extends TableName {
    val name: Fragment = fr"pga.fairwaylookup"
  }

  case object RoughLookup extends TableName {
    val name: Fragment = fr"pga.roughlookup"
  }

  case object SandLookup extends TableName {
    val name: Fragment = fr"pga.sandlookup"
  }

  case object RecoveryLookup extends TableName {
    val name: Fragment = fr"pga.recoverylookup"
  }

  case object GreenLookup extends TableName {
    val name: Fragment = fr"pga.greenlookup"
  }
}
