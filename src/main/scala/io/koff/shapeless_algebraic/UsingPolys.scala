package io.koff.shapeless_algebraic

import io.koff.shapeless_algebraic.BasicProtocol._
import io.koff.shapeless_algebraic.ExtendedProtocol._
import shapeless.{:+:, CNil, Poly1}

object UsingPolys {
  /* Ad Hoc ADT can be built from arbitrary types */
  type ReadRequests   = ReadUserInfo :+: GetAllUsers.type :+: CNil
  type WriteRequests  = CreateUser :+: UpdateUserInfo :+: DeleteUser :+: DeleteAllUsers.type :+: CNil

  object read extends Poly1 {
    implicit val readUserInfo = at[ReadUserInfo]    (_ => true)
    implicit val getAllUsers  = at[GetAllUsers.type](_ => 1)
  }

  object write extends Poly1 {
    implicit val createUser     = at[CreateUser]          (_ => true)
    implicit val updateUserInfo = at[UpdateUserInfo]      (_ => 1)
    implicit val deleteUser     = at[DeleteUser]          (_ => "2")
    implicit val deleteAllUsers = at[DeleteAllUsers.type] (_ => List(3))
  }

  private def readPatternMatch(request: ReadRequests): Unit = {
    val result = request.map(read)
  }

  private def writePatternMatch(request: WriteRequests): Unit = {
    val result = request.map(write)
  }
}
