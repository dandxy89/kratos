
[![Build Status](https://travis-ci.com/dandxy89/kratos.svg?branch=master)](https://travis-ci.com/dandxy89/kratos)

# Overview of Application

TODO: Overview of the project

# Strokes Gained

From PGA Tours Website:

    Strokes gained statistics have had a growing influence in the game of golf. 
    
    That should continue with the introduction of three new strokes gained statistics: strokes gained: off-the-tee, strokes gained: approach-the-green and strokes gained: around-the-green.
    
Measurements:

*   Stokes Gained = Off-the-tee + approach-the-green + around-the-green + putting
*   Strokes gained - off-the-tee: measures player performance off the tee on all par-4s and par-5s. 
*   Strokes gained - approach-the-green: measures player performance on approach shots. Approach shots include all shots that are not from the tee on par-4 and par-5 holes and are not included in strokes gained: around-the-green and strokes gained: putting. Approach shots include tee shots on par-3s.
*   Strokes gained - around-the-green: measures player performance on any shot within 30 yards of the edge of the green. This statistic does not include any shots taken on the putting green.    
*   Strokes Gained - Putting: how many strokes you gained or lost vs. PGA Tour average for putting
    
Worked example:
    
| Shot  | Location                                                  | Baseline from location | Next location       | Baseline from next location | Strokes gained               |
|-------|-----------------------------------------------------------|------------------------|---------------------|-----------------------------|------------------------------|
| 1     | 446 yards (tee box)                                       | 4.100                  | 116 yards (fairway) | 2.825                       | (4.100 - 2.825) - 1 = +0.275 |
| 2     | 116 yards (fairway)                                       | 2.825                  | 16' 11" (green)     | 1.826                       | (2.825 - 1.826) - 1 = -0.001 |
| 3     | 16' 11" (green)                                           | 1.826                  | Hole                | 0                           | (1.826 - 0) - 1 = +0.826     |
| Total | 446 yards (tee box)                                       | 4.100                  | Hole                | 0 (3 shots)                 | 4.100 - 3 = +1.100           |
|       | Strokes gained: total -- 0.275 + (-0.001) + 0.826 = 1.100 |                        |                     |                             |                              |

[Link](https://www.pgatour.com/news/2016/05/31/strokes-gained-defined.html)

## Setting up a local database

    docker run --rm   --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data postgres
    
    psql -h localhost -U postgres -d postgres

## Http4s Server

Not implemented

TODO

*   Registration
*   Authentication
*   Strokes gained
*   Saving to database
*   Email verification

## Testing

*   Unit tests - In Progress
*   Integration tests - To start
*   Pact tests - To start

## Scalajs / Purescript UI

TODO

*   Decide on ScalaJs or Purescript
*   Implement basic UI for demos
