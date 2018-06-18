package io.koff.shapeless_algebraic

/**
  * Problem - how to extend protocol without access to code of a sealed trait.
  * Solution - Wrapper + Evidence
  */
object ExtendedProtocol {
  sealed trait AdvancedRequest
  case object GetAllUsers     extends AdvancedRequest
  case object DeleteAllUsers  extends AdvancedRequest

  sealed trait AdvancedResponse
  case class AllUsers(names: Seq[String]) extends AdvancedResponse
  case class DeletedUsers(ids: Seq[Int])  extends AdvancedResponse
}
