package io.koff.syntax_tree

import cats.FlatMap

object DslTest {
  import cats.syntax.flatMap._

  implicit class ComplexDsl[F[_]: Algebra: FlatMap, T: Numeric](left: T) {
    private val alg = implicitly[Algebra[F]]
    def ?*? : Unit = println("call extension")
    def ?+? : Unit = println("call extension")
    def ^? : F[T] = alg.lift(left)
    def +?(right: T): F[T] = alg.add(left, right)
    def +?(right: F[T])(implicit d1: DummyImplicit): F[T] = right.flatMap(alg.add(left, _))
    def *?(right: T): F[T] = alg.mul(left, right)
    def *?(right: F[T])(implicit d1: DummyImplicit): F[T] = right.flatMap(alg.mul(left, _))
  }
  implicit class HigherDsl[F[_]: Algebra: FlatMap, T: Numeric](left: F[T]) {
    private val alg = implicitly[Algebra[F]]
    def ?*? : Unit = println("call extension")
    def ?+? : Unit = println("call extension")
    def ^? : F[T] = left
    def +?(right: T): F[T] = left.flatMap(alg.add(_, right))
    def *?(right: T): F[T] = left.flatMap(alg.mul(_, right))
    def *?(right: F[T])(implicit d1: DummyImplicit): F[T] = left.flatMap(l => right.flatMap(r => alg.mul(l, r)))
    //    def +?(right: F[T]): F[T] = ???
  }

  def main(args: Array[String]): Unit = {
    implicit val lazyAlg = LazyAlgebra

    2.?*?
    2.0.?+?
    val result = 2 *? 2 +? 2

    println(result)
    println(result.value)
  }
}
