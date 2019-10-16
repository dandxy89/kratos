SELECT  width_bucket(s.distance, 0, 120, 120 / 3) * 3 AS PuttDistanceWindow
        , COUNT(*) AS CountStrokes
FROM player.shot s
INNER JOIN (
    SELECT hole, MAX(shot) AS shot
    FROM player.shot
    WHERE game_id = 781
    GROUP BY hole
) j ON j.hole = s.hole AND s.shot = j.shot
WHERE s.ball_location = 6 AND game_id = 781
GROUP BY PuttDistanceWindow
