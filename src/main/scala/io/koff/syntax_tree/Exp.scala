package io.koff.syntax_tree

import cats.{Monad, ~>}
import cats.free.Free
import cats.free.Free.liftF
import io.koff.syntax_tree.Exp.{Add, FreeExp, Mul, Num}

import scala.concurrent.Future

sealed trait Exp[Result]

object Exp {
  type FreeExp[T] = Free[Exp, T]

  case class Add[T](left: Exp[T], right: Exp[T], numeric: Numeric[T]) extends Exp[T]
  case class Mul[T](left: Exp[T], right: Exp[T], numeric: Numeric[T]) extends Exp[T]
  case class Num[T](value: T) extends Exp[T]

  def add[T: Numeric](left: T, right: T): FreeExp[T] = {
    val num = implicitly[Numeric[T]]
    liftF[Exp, T](Add(Num(left), Num(right), num))
  }

  def mul[T: Numeric](left: T, right: T): FreeExp[T] = {
    val num = implicitly[Numeric[T]]
    liftF[Exp, T](Mul(Num(left), Num(right), num))
  }
}

object FreeAlgebra extends FAlgebra[FreeExp] {
  override def lift[T: Numeric](value: => T): FreeExp[T] = liftF[Exp, T](Num(value))
  override def add[T: Numeric](v1: T, v2: T): FreeExp[T] = Exp.add(v1, v2)
  override def mul[T: Numeric](v1: T, v2: T): FreeExp[T] = Exp.mul(v1, v2)
}

object FreeInterpreter {
  import cats.syntax.flatMap._

  def interpreter[F[_]: Monad]: Exp ~> F = new (Exp ~> F) {
    override def apply[A](fa: Exp[A]): F[A] = fa match {
      case Add(left, right, num) =>
        val leftF: F[A] = interpreter.apply(left)
        val rightF: F[A] = interpreter.apply(right)
        leftF.flatMap(l => rightF.flatMap(r => Monad[F].pure(num.plus(l, r))))

      case Mul(left, right, num) =>
        val leftF: F[A] = interpreter.apply(left)
        val rightF: F[A] = interpreter.apply(right)
        leftF.flatMap(l => rightF.flatMap(r => Monad[F].pure(num.times(l, r))))

      case Num(value) => Monad[F].pure(value)
    }
  }
}