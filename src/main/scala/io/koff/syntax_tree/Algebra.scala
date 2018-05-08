package io.koff.syntax_tree

import cats.effect.IO
import cats.{Eval, Id, Monad}

trait Algebra[F[_]] {
  def lift[T: Numeric](value: => T): F[T]
  def add[T: Numeric](v1: F[T], v2: F[T]): F[T]
  def mul[T: Numeric](v1: F[T], v2: F[T]): F[T]
}

object IdAlgebra extends Algebra[Id] {
  private def num[T: Numeric] = implicitly[Numeric[T]]
  override def add[T: Numeric](v1: T, v2: T): T = num.plus(v1, v2)
  override def mul[T: Numeric](v1: T, v2: T): T = num.times(v1, v2)

  override def lift[T: Numeric](value: => T): Id[T] = value
}


abstract class MonadicAlgebra[F[_]](implicit fm: Monad[F]) extends Algebra[F] {
  import cats.syntax.functor._
  import cats.syntax.flatMap._

  private def num[T: Numeric] = implicitly[Numeric[T]]
  override def add[T: Numeric](v1: F[T], v2: F[T]): F[T] = {
    v1.flatMap(l => v2.map(r => num.plus(l, r)))
  }
  override def mul[T: Numeric](v1: F[T], v2: F[T]): F[T] = {
    v1.flatMap(l => v2.map(r => num.times(l, r)))
  }
}

object LazyAlgebra extends MonadicAlgebra[Eval] {
  override def lift[T: Numeric](value: => T): Eval[T] = Eval.always(value)
}

object IOAlgebra extends MonadicAlgebra[IO] {
  override def lift[T: Numeric](value: => T): IO[T] = IO { value }
}
