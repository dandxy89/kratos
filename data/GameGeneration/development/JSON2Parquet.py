#!/usr/bin/env python
# -*- coding: utf-8 -*-
""" development/JSON2Parquet
"""
import json
import os
import re
from typing import Tuple, Any, Dict

import pandas as pd

regex_two = re.compile(".*?(\\d+).*?(\\d+)", re.IGNORECASE | re.DOTALL)

if "requirements.txt" not in os.listdir(os.getcwd()):
    os.chdir("../")


def get_path_data(txt: str) -> Tuple[str, str]:
    """ Extract the Tournament and Player Ids
    """
    m = regex_two.search(txt)
    if m:
        int1 = m.group(1)
        int2 = m.group(2)
        return int1, int2


def read_json(file_name: str) -> Dict[str, Any]:
    """ Read a JSON file to Dictionary
    """
    with open(file_name) as json_file:
        return json.load(json_file)


if __name__ == "__main__":
    root = "./resources"
    score_cards, summaries, overviews = [], [], []

    # Walk the directory and identify files of interest
    for dir_name, dirs, files in os.walk(root):
        if "scorecards" in dir_name:
            score_cards.append(dir_name)
        elif "summary.json" in files:
            summaries.append(f"{dir_name}/summary.json")
        elif "overview.json" in files:
            overviews.append(f"{dir_name}/overview.json")

    print(f"Number of Summary files to process: {len(summaries)}")
    player_storage = []

    # Grab the meta data from the summary files
    for each_summary_file in summaries:
        summary_1 = read_json(each_summary_file)
        for each_player in summary_1["leaderboard"]["players"]:
            bio = each_player["player_bio"]
            bio["player_id"] = each_player["player_id"]
            player_storage.append(bio)

    print(f"Number of Players: {len(player_storage)}")
    shot_storage = []
    card_counter = 0

    # Grab the round and shot data from the score cards
    for each_score_card in score_cards:
        for each_card in os.listdir(each_score_card):
            card_counter += 1

            score_cards_1 = read_json(f"{each_score_card}/{each_card}")
            (tournament_id, year), player_id = (
                get_path_data(each_score_card),
                int(score_cards_1["p"]["id"]),
            )
            for rnd_inx, round_ in enumerate(score_cards_1["p"]["rnds"]):
                for hole_inx, hole_ in enumerate(round_["holes"]):
                    for each_shot in hole_["shots"]:
                        each_shot["tournament_id"] = tournament_id
                        each_shot["year"] = year
                        each_shot["player_id"] = player_id
                        each_shot["round"] = rnd_inx + 1
                        each_shot["hole"] = hole_inx + 1
                        shot_storage.append(each_shot)

    print(f"{card_counter} cards processed totalling {len(shot_storage)} shots")
    player_storage = pd.DataFrame(player_storage)

    player_storage.to_parquet(
        "player_storage.parquet", engine="fastparquet", compression="gzip", index=False
    )

    shot_storage = pd.DataFrame(shot_storage)

    shot_storage.to_parquet(
        "shot_storage.parquet", engine="fastparquet", compression="gzip", index=False
    )
