package io.koff.shapeless_algebraic

import io.koff.shapeless_algebraic.BasicProtocol._
import io.koff.shapeless_algebraic.ExtendedProtocol._
import io.koff.shapeless_algebraic.UsingPolys.{ReadRequests, WriteRequests}
import shapeless._
import shapeless.ops.adjoin.Adjoin
/**
  * It is possble to work with complex Coproduct types easier by reusing & merging them
  */
object MergingCoproducts {
  val joined = Adjoin[WriteRequests :+: ReadRequests]
  type AllRequests = joined.Out

  trait read { self: Poly1 =>
    implicit val readUserInfo = at[ReadUserInfo]    (_ => true)
    implicit val getAllUsers  = at[GetAllUsers.type](_ => 1)
  }

  trait write { self: Poly1 =>
    implicit val createUser     = at[CreateUser]          (_ => 2.0F)
    implicit val updateUserInfo = at[UpdateUserInfo]      (_ => 3.0D)
    implicit val deleteUser     = at[DeleteUser]          (_ => "4")
    implicit val deleteAllUsers = at[DeleteAllUsers.type] (_ => List(5))
  }

  //Ad Hoc definition of a merged poly function
  object polyFunc extends Poly1 with read with write

  private def patternMatchForAllRequests(all: AllRequests) = {
    all.map(polyFunc)
  }

  /* It is possible to extract Coproducts from sealed traits */
  val genRequest    = Generic[Request]
  val genAdvRequest = Generic[AdvancedRequest]
  val joinedTraits  = Adjoin [genRequest.Repr :+: genAdvRequest.Repr]
  /* And join them together */
  type JoinedTraits = joinedTraits.Out

  private def patternMatchForAllTraits(all: JoinedTraits) = {
    all.map(polyFunc)
  }

  def main(args: Array[String]): Unit = {
    val coRequest1 = Coproduct[AllRequests](GetAllUsers)
    val coResult1 = patternMatchForAllRequests(coRequest1)
    val result1 = coResult1.select[Int]
    println(s"result1: $result1")

    val coRequest2 = Coproduct[JoinedTraits](DeleteAllUsers)
    val coResult2 = patternMatchForAllTraits(coRequest2)
    val result2 = coResult2.select[List[Int]]
    println(s"result2: $result2")
  }
}
