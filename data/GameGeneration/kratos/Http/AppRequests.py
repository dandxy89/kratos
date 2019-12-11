#!/usr/bin/env python
# -*- coding: utf-8 -*-
""" kratos/Http/AppRequests
"""
import json

import numpy as np
import requests as r

from kratos.Util.NumpyEncoder import NumpyEncoder


def create_game(auth_token: str, player_id: int):
    """ Create a game and return the Game ID
    """
    payload = {
        "playerId": player_id,
        "gameStartTime": 1568241871 + np.random.randint(30000),
        "courseName": "Burhill Golf Club - New Course",
        "handicap": 6.3,
        "ballUsed": "Truesoft",
        "temperature": 18.2,
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
            "Content-Type": "application/json",
        },
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
            "Content-Type": "application/json",
        },
    )

    return response.status_code
