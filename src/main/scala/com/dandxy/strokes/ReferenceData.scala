package com.dandxy.strokes

trait ReferenceData {
  def tableName: String
}

object ReferenceData {

  case object FairwayTable extends ReferenceData {
    val tableName: String = "FairwayLookup"
  }

  case object GreenTable extends ReferenceData {
    val tableName: String = "GreenLookup"
  }

  case object RecoveryTable extends ReferenceData {
    val tableName: String = "RecoveryLookup"
  }

  case object RoughTable extends ReferenceData {
    val tableName: String = "RoughLookup"
  }

  case object SandTable extends ReferenceData {
    val tableName: String = "SandLookup"
  }

  case object TeeTable extends ReferenceData {
    val tableName: String = "TeeLookup"
  }
}
