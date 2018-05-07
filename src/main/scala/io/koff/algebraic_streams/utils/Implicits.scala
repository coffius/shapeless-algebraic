package io.koff.algebraic_streams.utils

import cats.Applicative
import io.koff.algebraic_streams.algebras.Operations.nextPage
import io.koff.algebraic_streams.algebras.{Page, PageRequest}

object Implicits {
  implicit def unfoldPage[F[_]: Applicative, T]: Unfold[F, PageRequest, Page[T]] = {
    Unfold(PageRequest.FirstPage)(nextPage)
  }
}
