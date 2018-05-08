package io.koff.syntax_tree.exp

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