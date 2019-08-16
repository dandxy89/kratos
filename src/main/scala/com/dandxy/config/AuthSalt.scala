package com.dandxy.config

import com.dandxy.auth.Salt

final case class AuthSalt(salt: Option[Salt])
