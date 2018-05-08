package io.koff.syntax_tree

import cats.effect.IO
import io.koff.syntax_tree.HExp.{Add, Mul, Num}

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

trait HExpAlgebra {
  type C[T]
  final type F[T] = HExp[C, T]
  def liftF[T](value: => T): C[T]

  def lowAlg: Algebra[F] = new Algebra[F] {
    override def add[T: Numeric](v1: F[T], v2: F[T]): F[T] = Add[C, T](liftF(v1), liftF(v2))
    override def mul[T: Numeric](v1: F[T], v2: F[T]): F[T] = Mul[C, T](liftF(v1), liftF(v2))
    override def lift[T: Numeric](value: => T): F[T] = Num[C, T](liftF(value))
  }
}

object IOExpAlgebra extends HExpAlgebra {
  override type C[A] = IO[A]
  override def liftF[T](value: => T): IO[T] = IO { value }
}