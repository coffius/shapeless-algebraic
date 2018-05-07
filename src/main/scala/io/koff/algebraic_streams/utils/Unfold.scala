package io.koff.algebraic_streams.utils

import cats.Functor

trait Unfold[F[_], A, B] {
  def zero: A
  def unfold(a: A)(f: A => F[B]): F[Option[(B, A)]]
}

object Unfold {
  def apply[F[_]: Functor, A, B](init: A)(next: A => B => Option[(B, A)]): Unfold[F, A, B] = {
    new Unfold[F, A, B] {
      override def zero: A = init
      override def unfold(a: A)(f: A => F[B]): F[Option[(B, A)]] = {
        import cats.syntax.functor._
        val result = f(a)
        result.map(next(a))
      }
    }
  }
}
