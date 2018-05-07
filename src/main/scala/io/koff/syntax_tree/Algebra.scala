package io.koff.syntax_tree

import cats.effect.IO
import cats.{Eval, Id}

trait Algebra[F[_]] {
  def lift[T: Numeric](value: => T): F[T]
  def add[T: Numeric](v1: T, v2: T): F[T]
  def mul[T: Numeric](v1: T, v2: T): F[T]
}

object IdAlgebra extends Algebra[Id] {
  private def num[T: Numeric] = implicitly[Numeric[T]]
  override def add[T: Numeric](v1: T, v2: T): T = num.plus(v1, v2)
  override def mul[T: Numeric](v1: T, v2: T): T = num.times(v1, v2)

  override def lift[T: Numeric](value: => T): Id[T] = value
}


trait FAlgebra[F[_]] extends Algebra[F] {
  import io.koff.syntax_tree.{IdAlgebra => IdAl}
  override def add[T: Numeric](v1: T, v2: T): F[T] = lift(IdAl.add(v1, v2))
  override def mul[T: Numeric](v1: T, v2: T): F[T] = lift(IdAl.mul(v1, v2))
}

object LazyAlgebra extends FAlgebra[Eval] {
  override def lift[T: Numeric](value: => T): Eval[T] = Eval.always(value)
}

object IOAlgebra extends FAlgebra[IO] {
  override def lift[T: Numeric](value: => T): IO[T] = IO { value }
}
