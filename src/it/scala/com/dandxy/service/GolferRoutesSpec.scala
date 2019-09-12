// package com.dandxy.service

// import java.sql.Timestamp

// import cats.effect.IO
// import com.dandxy.jwt.GenerateToken
// import com.dandxy.model.player.PlayerId
// import org.scalatest.FlatSpec
// import com.dandxy.golf.entity.Location
// import com.dandxy.golf.input.Distance
// import com.dandxy.golf.pga.Statistic.PGAStatistic
// import com.dandxy.db.UserStore
// import cats.effect.Bracket
// import com.dandxy.auth.PlayerHash
// import com.dandxy.auth.PasswordAuth.{ hashPassword, verifyPassword }
// import com.dandxy.config.AuthSalt
// import com.dandxy.db.sql.TableName._
// import com.dandxy.golf.input.GolfInput.{ UserGameInput, UserShotInput }
// import com.dandxy.golf.input.{ Handicap, HandicapWithDate }
// import com.dandxy.model.player.PlayerId
// import com.dandxy.model.user.Identifier.{ GameId, Hole }
// import com.dandxy.model.user._
// import com.dandxy.strokes.GolfResult
// import doobie.implicits._
// import doobie.util.transactor.Transactor

// class GolferRoutesSpec extends FlatSpec {

//   behavior of "Golf Routes"

//   val testKey                                                            = "test_secret_key"
//   val validToken: String                                                 = GenerateToken.prepareToken(1, testKey)(PlayerId(1))
//   val mockGetStatistic: (Distance, Location) => IO[Option[PGAStatistic]] = ???

//   val mockUserStore = new UserStore[IO] {
//     def gdprPurge(playerId: PlayerId): IO[Int]                                                                      = ???
//     def registerUser(registration: UserRegistration, hashedPassword: Password, updateTime: Timestamp): IO[PlayerId] = ???
//     def attemptLogin(email: UserEmail, rawPassword: Password): IO[Option[PlayerId]]                                 = ???
//     def addClubData(playerId: PlayerId, input: List[GolfClubData]): IO[Int]                                         = ???
//     def getUserClubs(playerId: PlayerId): IO[List[GolfClubData]]                                                    = ???
//     def getAllPlayerGames(playerId: PlayerId): IO[List[UserGameInput]]                                              = ???
//     def getPlayerGame(gameId: GameId): IO[Option[UserGameInput]]                                                    = ???
//     def addPlayerGame(game: UserGameInput): IO[GameId]                                                              = ???
//     def deletePlayerGame(gameId: GameId): IO[Int]                                                                   = ???
//     def dropByHole(gameId: GameId, hole: Hole): IO[Int]                                                             = ???
//     def addPlayerShots(input: List[UserShotInput]): IO[Int]                                                         = ???
//     def getByGameAndMaybeHole(gameId: GameId, hole: Option[Hole]): IO[List[UserShotInput]]                          = ???
//     def getHandicapHistory(playerId: PlayerId): IO[List[HandicapWithDate]]                                          = ???
//     def aggregateGameResult(gameId: GameId): IO[List[AggregateGameResult]]                                          = ???
//     def addResultByIdentifier(result: GolfResult, h: Option[Hole]): IO[Int]                                         = ???
//     def getResultByIdentifier(game: GameId, h: Option[Hole]): IO[Option[GolfResult]]                                = ???
//     def getGameHandicap(game: GameId): IO[Option[Handicap]]                                                         = ???
//   }

//   val golfingRoute = GolferRoutes[IO](mockUserStore, testKey, mockGetStatistic).golferRoutes

//   it should "add clubs to the db" in {
//     pending
//   }
// }
