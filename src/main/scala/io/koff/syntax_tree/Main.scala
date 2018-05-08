package io.koff.syntax_tree

import io.koff.syntax_tree.Dsl._
import io.koff.syntax_tree.exp.{Exp, ExpAlgebra}
import io.koff.syntax_tree.hexp.IOExpAlgebra

import scala.math.Numeric.IntIsIntegral

object Main {
  def main(args: Array[String]): Unit = {
    lazyMain()
    ioMain()
    expMain()
    highExpMain()
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

  def expMain(): Unit = {
    implicit val expAlg: ExpAlgebra.type = ExpAlgebra

    val expResult: Exp[Int] = 4 *? 4 +? 4
    println(s"expResult: $expResult")
  }

  def highExpMain(): Unit = {
    implicit val alg = IOExpAlgebra.lowAlg

    val expResult = 5 *? 5 +? 5
    println(s"expResult: $expResult")
  }
}
