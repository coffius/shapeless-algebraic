package io.koff.syntax_tree


object Dsl {

  implicit class ComplexDsl[F[_]: Algebra, T: Numeric](left: T) {
    private val alg = implicitly[Algebra[F]]

    def +?(right: T): F[T] = alg.add(alg.lift(left), alg.lift(right))
    def +?(right: F[T])(implicit d1: DummyImplicit): F[T] = alg.add(alg.lift(left), right)
    def *?(right: T): F[T] = alg.mul(alg.lift(left), alg.lift(right))
    def *?(right: F[T])(implicit d1: DummyImplicit): F[T] = alg.mul(alg.lift(left), right)
  }

  implicit class HigherDsl[F[_]: Algebra, T: Numeric](left: F[T]) {
    private val alg = implicitly[Algebra[F]]

    def +?(right: T): F[T] = alg.add(left, alg.lift(right))
    def +?(right: F[T])(implicit d1: DummyImplicit): F[T] = alg.add(left, right)
    def *?(right: T): F[T] = alg.mul(left, alg.lift(right))
    def *?(right: F[T])(implicit d1: DummyImplicit): F[T] = alg.mul(left, right)
  }
}
