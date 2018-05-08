package io.koff.syntax_tree.hexp

import io.koff.syntax_tree.Algebra
import io.koff.syntax_tree.hexp.HExp.{Add, Mul, Num}

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