package io.koff.syntax_tree

import cats.Id
import io.koff.syntax_tree.Exp._

sealed trait Exp[Result]

object Exp {
  case class Add[T](
    left: Exp[T],
    right: Exp[T]
  ) extends Exp[T]

  case class Mul[T](
    left: Exp[T],
    right: Exp[T]
  ) extends Exp[T]

  case class Num[T](value: T) extends Exp[T]
}

object ExpAlgebra extends Algebra[Exp] {
  override def lift[T: Numeric](value: => T): Exp[T] = Num(value)
  override def add[T: Numeric](v1: Exp[T], v2: Exp[T]): Exp[T] = {
    Add(v1, v2)
  }

  override def mul[T: Numeric](v1: Exp[T], v2: Exp[T]): Exp[T] = {
    Mul(v1, v2)
  }
}