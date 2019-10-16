SELECT club, AVG((m.distance - n.distance)) AS AveragePuttDistance
FROM player.shot m
LEFT JOIN (
    SELECT distance, hole
    FROM player.shot
    WHERE game_id = 781 AND shot = 2
) n ON m.hole = n.hole
WHERE m.game_id = 781 AND shot = 1 AND (m.distance - n.distance) > 0 AND m.par > 3
GROUP BY club
ORDER BY AVG((m.distance - n.distance)) DESC
;