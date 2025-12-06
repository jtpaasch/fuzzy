package fuzzy

import fastparse.*
import fastparse.ScalaWhitespace.whitespace
import fuzzy.Formula.*


object Parser:

  /**
   * Parser for identifiers
   */
  private def identifier[$: P]: P[String] =
    P( CharsWhileIn("a-zA-Z").rep(1).! )

  /**
   * Parser for atomic names (identifiers).
   */
  private def atom[$: P]: P[Formula] =
    identifier.map(Atom.apply)

  /**
   * Parser for parenthesized expressions.
   */
  private def parens[$: P]: P[Formula] =
    P( "(" ~/ expr ~ ")" )

  /**
   * Base: atom, parenthesized expr, a negation
   */
  private def base[$: P]: P[Formula] =
    P(
      ( "!" ~/ base ).map(Not.apply)
      | parens
      | atom
    )

  /**
   * A conjunction "&&" is a conjunction of bases.
   * This matches last, so its precedence is highest.
   */
  private def conjunction[$: P]: P[Formula] =
    P( base.rep(1, sep = "&&") ).map{
      case Seq(result) => result
      case err => err.reduceLeft(And.apply)
    }

  /**
   * A disjunction "||" is a disjunction of conjunctions.
   * This matches second, so its precedence is next weakest.
   */
  private def disjunction[$: P]: P[Formula] =
    P( conjunction.rep(1, sep = "||") ).map{
      case Seq(result) => result
      case err => err.reduceLeft(Or.apply)
    }

  /**
   * An implication "->" is right-associative.
   * This matches first, so it is the weakest precedence.
   */
  private def implication[$: P]: P[Formula] =
    P( disjunction ~ ( "->" ~/ implication ).? ).map{
      case (lhs, Some(rhs)) => Implies(lhs, rhs)
      case (lhs, None) => lhs
    }

  /**
   * Expressions
   */
  private def expr[$: P]: P[Formula] = implication

  /**
   * Parse source text (a string) into a formula
   */
  def parse(source: String): Either[String, Formula] =
    fastparse.parse(source, source => expr(using source)) match
      case Parsed.Success(value, _) => Right(value)
      case err: Parsed.Failure =>
        Left(s"Parse error: ${err.msg}\n${err.trace().longMsg}")
