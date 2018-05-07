package io.koff.syntax_tree

import cats.{Eval, FlatMap}


object Dsl {
  import cats.syntax.flatMap._

  implicit class ComplexDsl[F[_]: Algebra: FlatMap, T: Numeric](left: T) {
    private val alg = implicitly[Algebra[F]]

    def ^? : F[T] = alg.lift(left)
    def +?(right: T): F[T] = alg.add(left, right)
    def +?(right: F[T])(implicit d1: DummyImplicit): F[T] = right.flatMap(alg.add(left, _))
    def *?(right: T): F[T] = alg.mul(left, right)
    def *?(right: F[T])(implicit d1: DummyImplicit): F[T] = right.flatMap(alg.mul(left, _))
  }
  implicit class HigherDsl[F[_]: Algebra: FlatMap, T: Numeric](left: F[T]) {
    private val alg = implicitly[Algebra[F]]

    def ^? : F[T] = left
    def +?(right: T): F[T] = left.flatMap(alg.add(_, right))
    def +?(right: F[T])(implicit d1: DummyImplicit): F[T] = ???
    def *?(right: T): F[T] = left.flatMap(alg.mul(_, right))
    def *?(right: F[T])(implicit d1: DummyImplicit): F[T] = left.flatMap(l => right.flatMap(r => alg.mul(l, r)))

  }
}
