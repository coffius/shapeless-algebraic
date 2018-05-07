package io.koff.algebraic_streams.algebras

import cats.MonadError
import fs2.{Pipe, Stream}
import io.koff.algebraic_streams.algebras.Pipes._
import io.koff.algebraic_streams.domain.Domain.User
import io.koff.algebraic_streams.utils.Implicits._
import io.koff.algebraic_streams.utils.Unfold

class EmailService(sender: EmailSender, userRepo: UserRepo) {
  private type UserPage   = Page[User]
  private type EmailPair  = (User, String)

  def sendToAll[F[_]](emailMsg: String)(implicit me: MonadError[F, Throwable]): Stream[F, Unit] = {

    unfold(userRepo.getAllUsers[F])
      .map(_.values)
      .through(flattenSeq)
      .map {
        user => Email(user.email, emailMsg)
      }
      .evalMap(sender.sendEmail[F])
  }
}

object Operations {
  type UserPage = Page[User]

  def nextPage[T](prevReq: PageRequest)(page: Page[T]): Option[(Page[T], PageRequest)] = {
    if (page.values.isEmpty) {
      None
    } else {
      Some((page, prevReq.next))
    }
  }
}

object Pipes {
  def combine[F[_], In, T](value: T): Pipe[F, In, (In, T)] = _.map((_, value))
  def flattenSeq[F[_], In]: Pipe[F, Seq[In], In] = _.flatMap(Stream.emits[In])
  def unfold[F[_], A, B](f: A => F[B])(implicit unfold: Unfold[F, A, B]): Stream[F, B] = {
    Stream.unfoldEval(unfold.zero)(unfold.unfold(_)(f))
  }
}