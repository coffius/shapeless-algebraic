package io.koff.algebraic_streams.algebras

import cats.Applicative
import io.koff.algebraic_streams.domain.Domain.User

trait UserRepo {
  def getAllUsers[F[_]: Applicative](pageRequest: PageRequest): F[Page[User]]
}

class FixedUserRepo extends UserRepo {
  private val total = 10000
  override def getAllUsers[F[_]: Applicative](pageRequest: PageRequest): F[Page[User]] = {
    Applicative[F].pure {
      val from = pageRequest.index * pageRequest.perPage
      val to = from + pageRequest.perPage
      val values = for {
        index <- from until Math.min(to, total)
      } yield {
        Thread.sleep(10)
        User(s"user#$index")
      }

      Page(pageRequest.index, total, values)
    }
  }
}

case class Page[T](pageIndex: Long, total: Long, values: Seq[T])

case class PageRequest(index: Long, perPage: Long){
  def next: PageRequest = {
    copy(index = index + 1)
  }
}

object PageRequest {
  val FirstPage = PageRequest(0, 10)
}