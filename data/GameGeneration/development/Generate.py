#!/usr/bin/env python
# -*- coding: utf-8 -*-
""" development/Generate

    Generate Game data

"""
import asyncio
import time

from kratos.GameGen.Constants import HOLE_CONFIG, GOLF_CLUBS, PUTT_LENGTH
from kratos.GameGen.GameGenerator import generate_course
from kratos.Http.AppRequests import create_game, put_request, get_request

PLAYER = 3
TOKEN = ""


async def generate_game_data():
    try:
        # Create a Game ID
        # server_game_id = 1
        server_game_id = create_game(TOKEN, PLAYER)

        # Generate a course worth of shots
        course_shots = generate_course(
            HOLE_CONFIG, GOLF_CLUBS, PUTT_LENGTH, server_game_id
        )
        print("Course count of shots: {}".format(len(course_shots)))
        # print(json.dumps(course_shots, cls=NumpyEncoder))

        # Add the shots to the DB
        _ = put_request("http://localhost:8080/golf/game/shot", TOKEN, course_shots)

        # Invoke the strokes gained calculation
        _ = get_request(
            "http://localhost:8080/golf/result/{}".format(server_game_id), TOKEN
        )
        print("Loaded Game ID: {}\n".format(server_game_id))

        return 1

    # Ignore recursion errors
    except RecursionError:
        return 0


async def main():
    job = [generate_game_data() for _ in range(10)]
    await asyncio.gather(*job)


if __name__ == "__main__":
    s = time.perf_counter()
    asyncio.run(main())
    elapsed = time.perf_counter() - s
    print(f"{__file__} executed in {elapsed:0.2f} seconds.")
