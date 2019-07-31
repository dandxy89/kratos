//package com.dandxy.strokes
//
//import com.dandxy.model.golf.entity.Hole
//import com.dandxy.model.golf.entity.Par.{ParFive, ParFour, ParThree}
//import com.dandxy.model.golf.input.GolfInput.UserCourseInput
//import com.dandxy.testData.SimulationTestData
//import org.scalatest.{FlatSpec, Matchers}
//
//class GameEngineSpec extends FlatSpec with Matchers with SimulationTestData {
//
//  val testHolesThree = List(
//    generateCompleteHoleData(Hole(1), ParThree, parThreeExample),
//    generateCompleteHoleData(Hole(2), ParFour, parFourExample),
//    generateCompleteHoleData(Hole(2), ParFive, parFiveExample)
//  )
//
//  val thereHoleCourse: UserCourseInput = generateCompleteGameData(testHolesThree)
//
//  GameEngine.calculateAll()
//
//}
