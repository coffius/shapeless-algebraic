package io.koff.syntax_tree.hexp

sealed trait HExp[F[_], Result]

object HExp {
  case class Add[F[_], T](
    left: F[HExp[F, T]],
    right: F[HExp[F, T]]
  ) extends HExp[F, T]

  case class Mul[F[_], T](
    left: F[HExp[F, T]],
    right: F[HExp[F, T]]
  ) extends HExp[F, T]

  case class Num[F[_], T](value: F[T]) extends HExp[F, T]
}