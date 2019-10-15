# Routes

LOGIN
* GET login + Authentication header - takes email:password and returns a JWT token if successful
* DONE

REGISTRATION
* PUT register + JSON payload - this takes the new users credentials and creates: profile, password, playerId. On registration it will send an email, the email will contain a hyperlink + JWT token to allow the user to verify their account

EMAIL VERIFICATION
* GET verify?token={Emailed JWT Token} - this is the link supplied in the verification email

GDPR Compliance (for ALEX :] )
* DELETE /gdpr/purge + Active JWT Token - this will delete the existence where possible all of the players details

(ADD / GET CLUB) DATA
* GET golf/club/update + Active JWT Token - this will retrieve the club data from storage
* PUT golf/club/update + Active JWT Token + JSON payload - this will take the data from JSON and persist

(GET) ALL GOLF GAMES
* GET golf/game/all?page={offset}  + Active JWT Token - this will paginate and get 5 at a time of the values from the database

(GET / ADD / DELETE) GAME DATE
* GET golf game {id} + Active JWT Token - this will retrieve a specific game from the database
* PUT /golf/game/{id} + Active JWT Token - this will generate a new game id
* DELETE /golf/game/{id} + Active JWT Token - this will generate a new game id

(GET / ADD ) SHOT DATA BY GAME AND HOLE
* GET /golf/game/{id}/hole/{optional id} + Active JWT Token - this will get all of the shot data from the database
* PUT /golf/game/shot + Active JWT Token + JSON payload of shots - this will add new shots / overwrite where required

(GET) HANDICAP HISTORY
* GET /golf/handicap/{player_id} + Active JWT Token - this will get get all handicaps recorded for the player

(GET) AGGREGATE GAME RESULT DATA by Hole or Game
* GET /golf/result/{game_id} + Active JWT Token - this will generate some summary statistics for a given game
* GET /golf/result/{game_id}/hole/{hole} + Active JWT Token - this will generate some summary statistics for a given game

(GET) PGA statistic
* GET /pga/stat/{}?distance={distance}&metric={metric} + Active JWT Token - get a PGA Statistic (1 to 6)
