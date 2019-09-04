
## Dan's TODO List

*   Add password verification - DONE
*   Add shots - DONE
*   Delete migrations - DONE
*   Add a game - DONE
*   Calculate stableford points - DONE
*   Basic stats - DONE
*   Store holes stats - DONE
*   Consolidate all tests into it:test - DONE
*   Interpolation of the missing data 1 yard/feet - DONE
*   Email verification table - DONE
*   Email marketing table - DONE
*   Interpolation of the missing data 1 yard/feet -> probability - DONE
*   Add more test coverage - DONE
*   Add http4s - DONE
*   Add more complex building blocks - token - DONE
*   Verify token
*   Add basic Auth
*   Add club data endpoint
*   Add game endpoint
*   Add shot endpoint
*   Add email marketing email endpoint
*   Add email verification endpoint + token + email
*   Add postman
*   Add stats endpoint
*   Compare to pga

#### Deferred items

*   Generate scorecard - DEFERRED (14/08/2019)
*   Generate stats (already proposed) More comprehensively below - DEFERRED (14/08/2019)
*   Get probability - DEFERRED (15/08/2019)

#### Statistics and Scorecards:

*   Holes result written to the dB
*   Games metrics:
*   Get ordered list of stokes gained by club
*   Get best x shots
*   Get worst x shots
*   Get strength by hole
*   Best X shots
*   Worst X shots
*   Greens in regulation
*   Fairways hit
*   Shot orientation per club
*   Average distance per club
*   Best performing club
*   Worst performing club
*   Displays without worst X shots
*   View by 3-6-9 hole splits
*   Scorecard view

#### Routes

LOGIN
*   GET /login + Authentication header - takes email:password and returns a JWT token if successful

REGISTRATION
*   PUT /register + JSON payload - this takes the new users credentials and creates: profile, password, playerId. On registration it will send an email, the email will contain a hyperlink + JWT token to allow the user to verify their account

EMAIL VERIFICATION
*   GET /verfiy?token={Emailed JWT Token} - this is the link supplied in the verification email

GDPR Compliance (for ALEX :] )
*   DELETE /gdpr/purge + Active JWT Token - this will delete the existence where possible all of the players details

(ADD / GET CLUB) DATA
*   POST /golf/club/update + Active JWT Token + JSON payload - this will take the data from JSON and persist
*   GET /golf/club/update + Active JWT Token - this will retrieve the club data from storage 

(GET) ALL GOLF GAMES
*   GET /golf/game/all?page={offset}  + Active JWT Token - this will paginate and get 5 at a time of the values from the database

(GET / ADD / DELETE) GAME DATE
*   GET /golf/game/{id} + Active JWT Token - this will retrieve a specific game from the database
*   PUT /golf/game/{id} + Active JWT Token - this will generate a new game id
*   DELETE /golf/game/{id} + Active JWT Token - this will generate a new game id

(GET / ADD / DELETE) SHOT DATA BY GAME AND HOLE
*   GET /golf/game/{id}/hole/{optional id} + Active JWT Token - this will get all of the shot data from the database
*   PUT /golf/game/shot + Active JWT Token + JSON payload of shots - this will add new shots / overwrite where required
*   DELETE /golf/game/{id}/hole/{id} + Active JWT Token - this will delete shots by hole id

(GET) HANDICAP HISTORY
*   GET /golf/handicap/{player_id} + Active JWT Token - this will get get all handicaps recorded for the player

(GET) AGGREGATE GAME RESULT DATA by Hole or Game
*   GET /golf/result/{game_id} + Active JWT Token - this will generate some summary statistics for a given game
*   GET /golf/result/{game_id}/hole/{hole} + Active JWT Token - this will generate some summary statistics for a given game

(GET) PGA statistic
*   GET /pga/stat/{}?distance={distance}&metric={metric} + Active JWT Token - get a PGA Statistic (1 to 6)
