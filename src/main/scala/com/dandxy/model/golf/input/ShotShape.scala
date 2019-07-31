package com.dandxy.model.golf.input

trait ShotShape {
  def description: String
}

object ShotShape {

  case object Straight extends ShotShape {
    val description: String = "Straight"
  }

  case object Fade extends ShotShape {
    val description: String = "Fade"
  }

  case object Draw extends ShotShape {
    val description: String = "Draw"
  }

  case object PushFade extends ShotShape {
    val description: String = "PushFade"
  }

  case object PushDraw extends ShotShape {
    val description: String = "PushDraw"
  }

  case object Slice extends ShotShape {
    val description: String = "Slice"
  }

  case object Hook extends ShotShape {
    val description: String = "Hook"
  }

  case object Topped extends ShotShape {
    val description: String = "Topped"
  }

  case object RoofedIt extends ShotShape {
    val description: String = "RoofedIt"
  }

  case object Stinger extends ShotShape {
    val description: String = "Stinger"
  }
}
