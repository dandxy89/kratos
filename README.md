# kratos-backend**

## Overview of Application

The purpose of this backend application is to record, calculate and analyse golf data.

The primary focus initially has been to incorporate the Strokes Gained methodology into the App whereby upon saving to the database the application will calculate the metrics for the given Hole and Game.

The intention longer term is to incorporate more analysis and modes to record shots but also different types of Games ( namely Sixes, Foursomes and Greensomes) and practice recording.

The ultimate goal is for this to assist with my own golf game - to take an analytical approach towards becoming a Scratch golfer.

## Strokes Gained

From PGA Tours Website:

    Strokes gained statistics have had a growing influence in the game of golf. 
    
    That should continue with the introduction of three new strokes gained statistics: strokes gained: off-the-tee, strokes gained: approach-the-green and strokes gained: around-the-green.

Measurements:

* Stokes Gained = Off-the-tee + approach-the-green + around-the-green + putting
* Strokes gained - off-the-tee: measures player performance off the tee on all par-4s and par-5s.
* Strokes gained - approach-the-green: measures player performance on approach shots. Approach shots include all shots that are not from the tee on par-4 and par-5 holes and are not included in strokes gained: around-the-green and strokes gained: putting. Approach shots include tee shots on par-3s.
* Strokes gained - around-the-green: measures player performance on any shot within 30 yards of the edge of the green. This statistic does not include any shots taken on the putting green.
* Strokes Gained - Putting: how many strokes you gained or lost vs. PGA Tour average for putting

Worked example:

| Shot  | Location                                                  | Baseline from location | Next location       | Baseline from next location | Strokes gained               |
|-------|-----------------------------------------------------------|------------------------|---------------------|-----------------------------|------------------------------|
| 1     | 446 yards (tee box)                                       | 4.100                  | 116 yards (fairway) | 2.825                       | (4.100 - 2.825) - 1 = +0.275 |
| 2     | 116 yards (fairway)                                       | 2.825                  | 16' 11" (green)     | 1.826                       | (2.825 - 1.826) - 1 = -0.001 |
| 3     | 16' 11" (green)                                           | 1.826                  | Hole                | 0                           | (1.826 - 0) - 1 = +0.826     |
| Total | 446 yards (tee box)                                       | 4.100                  | Hole                | 0 (3 shots)                 | 4.100 - 3 = +1.100           |
|       | Strokes gained: total -- 0.275 + (-0.001) + 0.826 = 1.100 |                        |                     |                             |                              |

[Link](https://www.pgatour.com/news/2016/05/31/strokes-gained-defined.html)

### Useful command

* Kill all dockers: ```docker kill $(docker ps -q)```
* Temporary database: ```docker run --rm   --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 postgres```
* Long-term development: ```docker run --rm   --name pg-docker -e POSTGRES_PASSWORD=docker -d -p 5432:5432 -v $HOME/docker/volumes/postgres:/var/lib/postgresql/data postgres```
* Command line connection to postgres: ```psql -h localhost -U postgres -d postgres```

### Http4s Server

Postman specification included within this project

### UI

Yet to be decided / built.

## Code Coverage

* ```sbt clean coverage it:test coverageReport``` - run the tests and generate a coverage report
* ```bash <(curl -s https://codecov.io/bash) -t 2136d990-e22d-4501-8293-501f554bf29f``` - push results to [codecov.io](https://codecov.io/gh/dandxy89/kratos)

## Code Count

* Scala: 4753 lines
* SQL: 3892
* JSON: 1354
* Markdown: 168
* R: 37

## Documents

All documents relating to this project are located [here](/docs)

Items include:

* Route descriptions
* Metric Ideas
* Project TODO list
* Deferrals

## Local Dependencies

You'll need to publish both `auth` and `middleware` projects locally:

* Auth - ```git clone git@github.com:dandxy89/kratos-auth.git```
* Middleware - ```git clone git@github.com:dandxy89/kratos-middlewares.git```

Then run the following to publish locally:

    sh publishLibraryLocally.sh
