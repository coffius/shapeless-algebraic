package io.koff.syntax_tree

import io.koff.syntax_tree.Dsl._
import io.koff.syntax_tree.Exp.FreeExp

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Main {
  def main(args: Array[String]): Unit = {
    lazyMain()
    ioMain()
    freeMain()
  }

  def lazyMain(): Unit = {
    implicit val lazyAlg = LazyAlgebra

    val evalResult = 2 *? 2 +? 2
    println(s"evalResult      : $evalResult")
    println(s"evalResult.value: ${evalResult.value}")
  }

  def ioMain(): Unit = {
    import cats.effect.IO
    implicit val ioAlg = IOAlgebra

    val ioResult: IO[Int] = 3 *? 3 +? 3
    println(s"ioResult      : $ioResult")
    println(s"ioResult.value: ${ioResult.unsafeRunSync()}")
  }

  def freeMain(): Unit = {
    import cats.instances.future._
    import scala.concurrent.ExecutionContext.Implicits.global

    implicit val freeAlg = FreeAlgebra

    val freeResult: FreeExp[Int] = 4 *? 4 +? 4
    val asyncResult = freeResult.foldMap(FreeInterpreter.interpreter[Future])
    val result = Await.result(asyncResult, Duration.Inf)
    println(s"freeResult: $freeResult")
    println(s"result: $result")
  }
}
