package io.koff.shapeless_algebraic

import io.koff.shapeless_algebraic.SealedTrait.BasicProtocol._
import io.koff.shapeless_algebraic.SealedTrait.ExtendedProtocol.{GetAllUsers, _}
import io.koff.shapeless_algebraic.SealedTrait.WrapperExtension.Wrapper.Evidence
import shapeless._

object SealedTrait {
  //TODO: What is ADT?
  //TODO: Where/Why are ADT's used?

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


    //TODO: Exhaustiveness Checking for sealed traits
    //TODO: compiler flag warn-2-error
    private def exhaustivePatternMatch(request: Request) = {
      request match {
        case CreateUser(name)         => ???
        case ReadUserInfo(id)         => ???
        case UpdateUserInfo(id, name) => ???
        case DeleteUser(id)           => ???
      }
    }

    // with `-Xfatal-warnings` non-exhaustive matches will not compile
    private def nonExhaustivePatternMatch(response: Response) = {
      response match {
        case res: UserCreated  => ???
        case res: UserInfo     => ???
        case res: UserUpdated  => ???
        case res: UserDeleted  => ???
      }
    }
  }

  // Problem - how to extend protocol without access to code of a sealed trait
  // Solution - Wrapper + Evidence
  object ExtendedProtocol {
    sealed trait AdvancedRequest
    case object GetAllUsers     extends AdvancedRequest
    case object DeleteAllUsers  extends AdvancedRequest

    sealed trait AdvancedResponse
    case class AllUsers(names: Seq[String]) extends AdvancedResponse
    case class DeletedUsers(ids: Seq[Int])  extends AdvancedResponse
  }

  object WrapperExtension {
    case class Wrapper[T: Evidence](value: T)
    object Wrapper {
      sealed trait Evidence[-T]
      implicit object RequestEvidence extends Evidence[Request]
      implicit object AdvancedRequestEvidence extends Evidence[AdvancedRequest]
    }

    val withBasicRequest = Wrapper(CreateUser("test-user"))
    val withAdvancedRequest = Wrapper(GetAllUsers)
    // does not compile with `withInvalidValue` uncommented
    //val withInvalidValue = Wrapper(DeletedUsers(Seq(1, 2)))

    // Problem - how to make a compiler to check Exhaustiveness of the extended protocol(Wrapper + Evidence)
    private def patternMatch[T: Evidence](wrapper: Wrapper[T]) = {
      // compiles with several matches missed - no warns or errors :(
      wrapper.value match {
        case CreateUser(name)         => ???
        case ReadUserInfo(id)         => ???
      }
    }
  }

  // Solution - shapeless Coproduct + Poly
  object UsingPolys {
    // Ad Hoc ADT is built from arbitrary types
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

    private def readMatch(request: ReadRequests) = {
      val result = request.map(read)
    }

    private def writeMatch(request: WriteRequests) = {
      val result = request.map(write)
    }
  }

  // Addition#1 coproducts {}
  // - merging Coproducts
  object MergingCoproducts {
    /*
    val joined = Adjoin[ADT1.CR :+: ADT2.UD]
  type CRUD_combined = joined.Out
     */
  }
  // Addition#2 - merging Polys
  // Addition#3 - ADT's in other languages(Idris)
}
