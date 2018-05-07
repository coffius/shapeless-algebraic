package io.koff.algebraic_streams.domain

object Domain {
  final case class User(email: String)
  final case class EmailMessage(template: String)
}
