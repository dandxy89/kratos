package com.dandxy.middleware

import com.dandxy.middleware.http4s.instances.EntityInstances
import org.http4s.{ MediaRange, Response }

import scala.language.higherKinds

package object http4s extends ToResponseInstances with EntityInstances {

  type ToHttpResponse[F[_], T] = ToResponse[F, List[MediaRange], Response[F], T]

}
