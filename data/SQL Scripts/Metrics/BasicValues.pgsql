SELECT
    SUM(CASE WHEN s.shot = s.par - 2 THEN 1 ELSE 0 END) AS Eagle
    , SUM(CASE WHEN s.shot = s.par - 1 THEN 1 ELSE 0 END) AS Birdie
    , SUM(CASE WHEN s.shot = s.par THEN 1 ELSE 0 END) AS Par
    , SUM(CASE WHEN s.shot = s.par + 1 THEN 1 ELSE 0 END) AS Bogey
    , SUM(CASE WHEN s.shot = s.par + 2 THEN 1 ELSE 0 END) AS DoubleBogey
    , SUM(CASE WHEN s.shot = s.par + 3 THEN 1 ELSE 0 END) AS TripleBogey
    , SUM(CASE WHEN s.shot > s.par + 3 THEN 1 ELSE 0 END) AS OtherBogey
FROM player.shot s
INNER JOIN (
    SELECT hole, MAX(shot) AS shot
    FROM player.shot
    WHERE game_id = 781
    GROUP BY hole
) j ON j.hole = s.hole AND s.shot = j.shot
WHERE game_id = 781
GROUP BY game_id
