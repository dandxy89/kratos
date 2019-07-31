
INSERT INTO player.playerlookup (player_email, update_time, first_name, last_name)
VALUES ('test@dandxy.com', '01-01-2019 13:01:02', 'dan', 'dixey')
RETURNING player_id;

INSERT INTO usersecurity.hashedpassword (player_email, hashed_password, player_id)
VALUES ('test@dandxy.com', '123456', 1)
;

INSERT INTO player.game (player_id, course, game_start_time, handicap)
VALUES (1, 'Dans Course', to_timestamp('02-01-2019 13:01:02', 'dd-mm-yyyy hh24:mi:ss'), 7.5)
RETURNING game_id;

INSERT INTO player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained)
VALUES (1, 1, 1, 4, 446, 1, 1, 0.275);

INSERT INTO player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained)
VALUES (1, 1, 2, 4, 116, 2, 22, -0.001);

INSERT INTO player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained)
VALUES (1, 2, 3, 4, 17, 6, 25, 0.826);
