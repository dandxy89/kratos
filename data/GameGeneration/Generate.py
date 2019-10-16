import json
import asyncio
import numpy as np
import pandas as pd
import requests as r


class NumpyEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, np.integer):
            return int(obj)
        elif isinstance(obj, np.floating):
            return float(obj)
        elif isinstance(obj, np.ndarray):
            return obj.tolist()
        else:
            return super(NumpyEncoder, self).default(obj)


PLAYER = 3
TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJnb2xmZXIiLCJleHAiOjE1NzEyNjU0NzYsImlhdCI6MTU3MTI1ODI3NiwicGxheWVySWQiOjN9.JIJTyanm2U4EJUxV8JxyvFejHAV69TF6M_FqiTHvj8w"

HOLE_CONFIG = [
    # Course configuration
    # N_HOLE, MIN_S, MAX_S, MIN_D, MAX_D, Label, Par, Norm_shots_to_hole, Duff_shot_min_id
    # 0, 1, 2, 3, 4, 5, 6, 7, 8
    (6, 2, 6, 100, 230, "Par 3", 3, 1, 15, (15, 22), (), ()),
    (6, 3, 7, 285, 475, "Par 4", 4, 2, 19, (13, 16), (15, 23), ()),
    (6, 3, 8, 380, 579, "Par 5", 5, 3, 22, (13, 14), (15, 21), (18, 21))
]

GOLF_CLUBS = [
    # Golf club configuration
    (25, 0, 30),  # PUTTER
    (24, 25, 60),  # WEDGE_LW
    (23, 55, 89),  # WEDGE_SW
    (22, 85, 110),  # WEDGE_GW
    (21, 105, 130),  # WEDGE_PW
    (20, 120, 145),  # IRON9
    (19, 135, 160),  # IRON8
    (18, 150, 175),  # IRON7
    (17, 170, 190),  # IRON6
    (16, 180, 205),  # IRON5
    (15, 200, 225),  # IRON4
    (14, 215, 260),  # WOOD5 # Actual ID 4
    (13, 260, 330)  # Driver # Actual ID 1
]

PUTT_LENGTH = [
    # N_PUTTS, LIMIT_LENGTH
    (1, 25),
    (2, 15),
    (3, 7)
]

DUFF_SHAPE = [6, 7, 8, 9]


def generator(min, max):
    """ Bounder random selection to Integer
    """
    return np.int(np.random.uniform(min, max))


def find_club_id(clubs, id):
    """ Find club params by ID
    """
    return list(filter(lambda x: x[0] == id, clubs))[0]


def putt_length(limit):
    """ Putt length generator of at least 1 yard
    """
    return np.int(np.random.uniform() * limit) + 1


def fetch_putt(par, count):
    """ Calculate the number of putts

        20% chance of converting a 3 -> 2 putt
        20% chance of converting a 2 -> 1 putt
    """
    if count > par:
        return 3 - (1 if np.random.uniform() < 0.2 else 0)
    elif count == par:
        return 2 - (1 if np.random.uniform() < 0.2 else 0)
    else:
        return 1


def duff_generator(min, max):
    """ Generate a duff shot of a given length of at least 1 yard
    """
    return np.int(generator(min, max) * 0.45) + 1


def random_hole_selection(par_three, par_four, par_five):
    """ Select a hole within the constraints of the config
    """
    rv = np.random.randint(3)

    if rv == 0 and par_three[1] < par_three[0]:
        return (rv, ((par_three[0], par_three[1] + 1), par_four, par_five), 3)
    elif rv == 1 and par_four[1] < par_four[0]:
        return (rv, (par_three, (par_four[0], par_four[1] + 1), par_four), 4)
    elif rv == 2 and par_five[1] < par_five[0]:
        return (rv, (par_three, par_four, (par_five[0], par_five[1] + 1)), 5)
    else:
        # Recursively function will return once a holes been selected
        return random_hole_selection(par_three, par_four, par_five)


def shot_generator(config, clubs, remaining_shot, hole_type, remaining_dist, duff, game_id, index, hole_par):
    """ Shot generator
    """
    accum = 0
    accum_shot = 0
    hole_index = index
    index = remaining_shot - config[hole_type][7] if duff else remaining_shot
    hits = []

    for ind in np.arange(index):
        if duff:
            club = generator(config[hole_type][8], 24)
            shot_distance = duff_generator(0, find_club_id(clubs, club)[2])

        else:
            club = generator(config[hole_type][9 + ind]
                             [0], config[hole_type][9 + ind][1])
            shot_distance = generator(find_club_id(clubs, club)[
                                      1], find_club_id(clubs, club)[2])

        accum += shot_distance
        accum_shot += 1

        payload = {
            "gameId": game_id,
            "hole": hole_index + 1,
            "shot": 1 + ind,
            "par": hole_par,
            "distance": shot_distance,
            "location": 1 if (remaining_shot - (ind - 1)) == 1 else 2,  # TODO
            "club": club,
            "strokeIndex": index + 1
        }

        hits.append(payload)

    return (accum, accum_shot, hits)


def generate_putts(putt_config, n_putt, game_id, index, shots_remaining, hole_par):
    """ Generate putt data
    """
    putt_lengths = [putt_length(putt_config[p][1]) for p in np.arange(n_putt)]
    putt_lengths.sort()
    all_shots = []

    for pl in putt_lengths:
        # Generate payload
        payload = {
            "gameId": game_id,
            "hole": index + 1,
            "shot": shots_remaining,
            "par": hole_par,
            "distance": pl,
            "location": 6,
            "club": 25,
            "strokeIndex": index + 1
        }

        shots_remaining -= 1
        all_shots.append(payload)

    return all_shots


def combine_list(combined, new_list):
    """ Appends two lists
    """
    for s in new_list:
        combined.append(s)

    return combined


def generate_hole(config, clubs, criteria, putt_config, game_id, stroke_index):
    """ Generate a hole output
    """
    # Select a hole to generate
    (hole_type, criteria, hole_par) = random_hole_selection(
        criteria[0], criteria[1], criteria[2])
    all_shots = []

    # Determine the number of shots to be taken
    shot_count = generator(config[hole_type][1], config[hole_type][2])

    # Determine the distance
    total_distance = 0
    dist = generator(config[hole_type][3], config[hole_type][4])
    n_putt = fetch_putt(config[hole_type][6], shot_count)

    # Generator putt lengths
    putts = generate_putts(putt_config, n_putt, game_id,
                           stroke_index, shot_count, hole_par)
    all_shots = combine_list(all_shots, putts)

    # Shots remaining
    remaining_shot = shot_count - n_putt

    # Duff shot setup
    if (remaining_shot - config[hole_type][7]) > 0:
        (accum, accum_shot, hits) = shot_generator(
            config, clubs, remaining_shot, hole_type, 0, True, game_id, stroke_index, hole_par)

        # Remaining distances and shots
        dist -= accum
        remaining_shot -= accum_shot
        total_distance += accum
        all_shots = combine_list(all_shots, hits)

    # Take the remaining shots
    (accum, accum_shot, hits) = shot_generator(
        config, clubs, remaining_shot, hole_type, dist, False, game_id, stroke_index, hole_par)
    all_shots = combine_list(all_shots, hits)

    # Remaining distances and shots
    total_distance += accum

    return (criteria, shot_count, total_distance, all_shots)


def generate_course(config, clubs, putt_config, game_id):
    """ Generate Course
    """
    # Determine the number of holes to generate
    shot_count = 0
    total_distance = 0
    num_hole = np.sum([item[0] for item in config])
    course_shots = []

    # Selection crieteria
    criteria = ((config[0][0], 0), (config[1][0], 0), (config[2][0], 0))

    # Generate a hole via a loop
    for stroke_index in np.arange(num_hole):

        # Run the generate hole function
        (s, sc, td, hs) = generate_hole(config, clubs,
                                        criteria, putt_config, game_id, stroke_index)
        criteria = s
        shot_count += sc
        total_distance += td
        hs.reverse()

        # Append all of the shots
        course_shots = combine_list(course_shots, hs)

    # Print final shot count
    print("Final shot count:: {}".format(shot_count))
    print("Final shot distance:: {}".format(total_distance))

    return course_shots


def create_game(auth_token, playerId):
    """ Create a game and return the Game ID
    """
    payload = {
        "playerId": playerId,
        "gameStartTime": 1568241871 + np.random.randint(30000),
        "courseName": "Burhill Golf Club - New Course",
        "handicap": 6.3,
        "ballUsed": "Truesoft",
        "temperature": 18.2
    }

    return put_request("http://localhost:8080/golf/game", auth_token, payload)


def put_request(url, auth_token, payload):
    """ Pre-defined PUT request
    """
    response = r.put(
        url=url,
        data=json.dumps(payload, cls=NumpyEncoder),
        headers={
            "Authorization": "Bearer {}".format(auth_token),
            "Accept": "application/json",
            "Content-Type": "application/json"
        }
    )

    return response.text


def get_request(url, auth_token):
    """ Pre-defined GET request
    """
    response = r.get(
        url=url,
        headers={
            "Authorization": "Bearer {}".format(auth_token),
            "Accept": "application/json",
            "Content-Type": "application/json"
        }
    )

    return response.status_code


async def generate_game_data():
    try:
        # Create a Game ID
        #server_game_id = 1
        server_game_id = create_game(TOKEN, PLAYER)

        # Generate a course worth of shots
        course_shots = generate_course(
            HOLE_CONFIG, GOLF_CLUBS, PUTT_LENGTH, server_game_id)
        print("Course count of shots: {}".format(len(course_shots)))
        #print(json.dumps(course_shots, cls=NumpyEncoder))

        # Add the shots to the DB
        shots_added = put_request(
            "http://localhost:8080/golf/game/shot", TOKEN, course_shots)

        # Invoke the strokes gained calculation
        status_code = get_request(
            "http://localhost:8080/golf/result/{}".format(server_game_id), TOKEN)
        print("Loaded Game ID: {}\n".format(server_game_id))

        return 1

    # Ignore recursion errors
    except RecursionError:
        return 0


async def main():
    coros = [generate_game_data() for _ in range(1000)]
    await asyncio.gather(*coros)


if __name__ == "__main__":
    import time
    s = time.perf_counter()
    asyncio.run(main())
    elapsed = time.perf_counter() - s
    print(f"{__file__} executed in {elapsed:0.2f} seconds.")
