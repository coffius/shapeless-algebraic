package io.koff.shapeless_algebraic

import io.koff.shapeless_algebraic.SealedTrait.BasicProtocol._

/**
  * Examples of pattern matching
  */
object PatternMatching {
  /**
    * Exhaustive pattern match - contains case ... => ... for every type in ADT.
    * Total function - is defined for all possible types
    */
  private def exhaustivePatternMatch(request: Request) = {
    request match {
      case CreateUser(name)         => ???
      case ReadUserInfo(id)         => ???
      case UpdateUserInfo(id, name) => ???
      case DeleteUser(id)           => ???
    }
  }
  /**
    * Non-Exhaustive pattern match - misses one of types from ADT.
    * Partial function - is undefined for some types
    * with `-Xfatal-warnings` non-exhaustive matches will not compile
    */
  private def nonExhaustivePatternMatch(response: Response) = {
    response match {
      case res: UserCreated  => ???
      case res: UserInfo     => ???
      case res: UserUpdated  => ???
      case res: UserDeleted  => ???
    }
  }
}
