package io.koff.algebraic_streams

import cats.data.EitherT
import cats.{Eval, MonadError}
import io.koff.algebraic_streams.algebras.{Email, EmailSender, EmailService, UserRepo}
import io.koff.algebraic_streams.domain.Domain.User
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FreeSpec, MustMatchers}

class EmailServiceSpec extends FreeSpec with MustMatchers with MockFactory {
  private type Result[T] = EitherT[Eval, Throwable, T]
  private type EmailPair  = (User, String)

  private val testMessage = "test msg"
  private val sender    = {
    val m = mock[EmailSender]
    (m.sendEmail[Result](_: Email)(_: MonadError[Result, Throwable]))
      .expects(where((email, _) => email.message == testMessage))
        .returning(EitherT.pure(()))
    m
  }
  private val userRepo  = mock[UserRepo]

  "EmailService" - {
    "sends emails" in {
      val service = new EmailService(sender, userRepo)

      val stream = service.sendToAll[Result](testMessage)
      val _ = stream.compile.drain.value.value // execute calculations
    }
  }
}
