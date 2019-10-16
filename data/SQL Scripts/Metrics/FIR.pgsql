SELECT  game_id, 
        SUM(CASE WHEN ball_location = 2 THEN 1 ELSE 0 END) AS GreenCount, 
        COUNT(ball_location) AS TotalHolesPlayed
FROM "player"."shot" 
WHERE   game_id = 781
        AND (
            par = 4 and shot = 2
            OR par = 5 and shot = 2
        )
GROUP BY game_id
;
