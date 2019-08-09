-- PLAYER LOOKUP
----------------
-- P1
insert into player.playerlookup (player_email, update_time, first_name, last_name)
values ('test@dandxy.com', '01-01-2019 13:01:02', 'dan', 'dixey');
-- P2
insert into player.playerlookup (player_email, update_time, first_name, last_name)
values ('gdpr@dandxy.com', '01-01-2019 13:01:02', 'dan', 'dixey');

-- SECURITY
-----------
-- P1
insert into usersecurity.hashedpassword (player_email, hashed_password, player_id)
values ('test@dandxy.com', '123456', 1);
-- P2
insert into usersecurity.hashedpassword (player_email, hashed_password, player_id)
values ('gdpr@dandxy.com', '123456', 2);

-- GAME
-------
insert into player.game (player_id, course, game_start_time, handicap)
values (1, 'Dans Course', to_timestamp('02-01-2019 13:01:02', 'dd-mm-yyyy hh24:mi:ss'), 7.5);

-- P2 - Game 1
insert into player.game (player_id, course, game_start_time, handicap)
values (2, 'GDPR Course', to_timestamp('03-01-2019 13:01:02', 'dd-mm-yyyy hh24:mi:ss'), 8.0);

-- P1 Game 2
insert into player.game (player_id, course, game_start_time, handicap)
values (1, 'Dans Course 2', to_timestamp('03-01-2019 13:01:02', 'dd-mm-yyyy hh24:mi:ss'), 7.1);

-- CLUB DATA
------------
-- P1
insert into player.club_data (player_id, club, typical_shape, typical_height, manufacturer, typical_distance, distanceType)
values (1, 1, 1, 1, 1, 200, 1);
-- P2
insert into player.club_data (player_id, club, typical_shape, typical_height, manufacturer, typical_distance, distanceType)
values (2, 1, 1, 1, 1, 200, 1);

-- SHOT DATA
------------
-- P1
-- GAME 1
insert into player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
values (1, 1, 1, 4, 446, 1, 1, 0.275, 2);

insert into player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
values (1, 1, 2, 4, 116, 2, 22, -0.001, 2);

insert into player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
values (1, 2, 3, 4, 17, 6, 25, 0.826, 2);

insert into player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
values (3, 1, 1, 4, 446, 1, 1, 0.275, 2);

insert into player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
values (3, 1, 2, 4, 116, 2, 22, -0.001, 2);

insert into player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
values (3, 2, 3, 4, 17, 6, 25, 0.826, 2);

-- P2
insert into player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
values (2, 1, 1, 4, 446, 1, 1, 0.275, 2);

insert into player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
values (2, 1, 2, 4, 116, 2, 22, -0.001, 2);

insert into player.shot (game_id, hole, shot, par, distance, ball_location, club, strokes_gained, stroke_index)
values (2, 2, 3, 4, 17, 6, 25, 0.826, 2);
