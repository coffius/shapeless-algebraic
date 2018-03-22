package io.koff.shapeless_algebraic

object SealedTrait {
  object Protocol {
    sealed trait Request
    case class CreateUser(name: String)                   extends Request
    case class ReadUserInfo(userId: Int)                  extends Request
    case class UpdateUserInfo(userId: Int, name: String)  extends Request
    case class DeleteUser(userId: Int)                    extends Request

    sealed trait Response
    case class UserCreated(id: Int, name: String)     extends Response
    case class UserInfo   (name: String)              extends Response
    case class UserUpdated(userId: Int, name: String) extends Response
    case class UserDeleted(userId: Int)               extends Response

    //TODO: Exhaustiveness Checking for sealed traits
    //TODO: compiler flag warn-2-error
    // Problem - how to extend protocol without access to code of a sealed trait
    // Solution - Wrapper + Evidence
    // Problem - how to make a compiler to check Exhaustiveness of extended protocol(Wrapper + Evidence)
    // Solution - shapeless Coproduct + Poly
    // Addition#1 - merging Coproducts
    // Addition#2 - merging Polys
  }
}
