package io.koff.syntax_tree.exp

import io.koff.syntax_tree.Algebra
import io.koff.syntax_tree.exp.Exp._

object ExpAlgebra extends Algebra[Exp] {
  override def lift[T: Numeric](value: => T): Exp[T] = Num(value)
  override def add[T: Numeric](v1: Exp[T], v2: Exp[T]): Exp[T] = {
    Add(v1, v2)
  }

  override def mul[T: Numeric](v1: Exp[T], v2: Exp[T]): Exp[T] = {
    Mul(v1, v2)
  }
}
