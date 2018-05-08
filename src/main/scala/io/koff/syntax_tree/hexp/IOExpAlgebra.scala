package io.koff.syntax_tree.hexp

import cats.effect.IO

object IOExpAlgebra extends HExpAlgebra {
  override type C[A] = IO[A]
  override def liftF[T](value: => T): IO[T] = IO { value }
}
