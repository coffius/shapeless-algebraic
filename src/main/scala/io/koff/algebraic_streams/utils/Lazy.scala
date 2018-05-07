package io.koff.algebraic_streams.utils

trait Lazy[F[_]] {
  def delay[T](pure: => T): F[T]
}

object Lazy {
  def delay[F[_]: Lazy, T](pure: => T): F[T] = {
    val laziness = implicitly[Lazy[F]]
    laziness.delay(pure)
  }
}
