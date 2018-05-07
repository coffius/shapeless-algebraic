package io.koff.algebraic_streams.algebras

trait EmailClient {
  def send(email: String, what: String): Unit
}

object FlakyEmailClient extends EmailClient {
  override def send(email: String, what: String): Unit = {
    Thread.sleep(123)
    if(System.currentTimeMillis() % 3 == 0)
      println(s"FakeEmailClient: send `$what` to `$email`")
    else
      throw new IllegalStateException("wrong place, wrong time")
  }
}

object FastEmailClient extends EmailClient {
  override def send(email: String, what: String): Unit = {
    println(s"FakeEmailClient: send `$what` to `$email`")
  }
}
