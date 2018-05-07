package io.koff.algebraic_streams.algebras

import cats.MonadError

import scala.util.control.NonFatal

trait EmailSender {
  def sendEmail[R[_]](toSend: Email)(implicit me: MonadError[R, Throwable]): R[Unit]
}

case class Email(address: String, message: String)

class EmailSenderImpl(client: EmailClient) extends EmailSender {
  override def sendEmail[R[_]](toSend: Email)(implicit me: MonadError[R, Throwable]): R[Unit] = {
    me.catchNonFatal {
      client.send(toSend.address, toSend.message)
    }
  }
}
