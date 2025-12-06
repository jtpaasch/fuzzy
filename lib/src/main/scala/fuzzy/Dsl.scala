package fuzzy

import fuzzy.Formula.*

object Dsl:

  extension (lhs: Formula)
    def &&(rhs: Formula): Formula = And(lhs, rhs)
    def ||(rhs: Formula): Formula = Or(lhs, rhs)
    def ->(rhs: Formula): Formula = Implies(lhs, rhs)

  extension (f: Formula)
    def unary_! : Formula = Not(f)

  def atom(name: String): Formula = Atom(name)
