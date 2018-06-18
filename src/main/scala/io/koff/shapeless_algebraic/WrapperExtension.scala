package io.koff.shapeless_algebraic

import io.koff.shapeless_algebraic.BasicProtocol._
import io.koff.shapeless_algebraic.ExtendedProtocol._
import io.koff.shapeless_algebraic.WrapperExtension.Wrapper.Evidence

object WrapperExtension {
  case class Wrapper[T: Evidence](value: T)
  object Wrapper {
    sealed trait Evidence[-T]
    implicit object RequestEvidence extends Evidence[Request]
    implicit object AdvancedRequestEvidence extends Evidence[AdvancedRequest]
  }

  val withBasicRequest = Wrapper(CreateUser("test-user"))
  val withAdvancedRequest = Wrapper(GetAllUsers)

  /* ↓↓↓ does not compile with `withInvalidValue` uncommented ↓↓↓  */
//  val withInvalidValue = Wrapper(DeletedUsers(Seq(1, 2)))

  /**
    * Problem - how to make a compiler to check Exhaustiveness
    * of the extended protocol(Wrapper + Evidence)
    */
  private def patternMatch[T](wrapper: Wrapper[T]) = {
    // compiles with several matches missed - no warns or errors :(
    wrapper.value match {
      case CreateUser(name) => ???
      case ReadUserInfo(id) => ???
    }
  }
}
