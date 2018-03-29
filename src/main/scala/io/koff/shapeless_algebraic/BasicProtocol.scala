package io.koff.shapeless_algebraic

/**
  * Example of ADT - a simple protocol that defines CRUD operations for a user
  */
object BasicProtocol {
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
}
