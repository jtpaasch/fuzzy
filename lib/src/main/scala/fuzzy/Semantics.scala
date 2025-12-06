package fuzzy

import fuzzy.Core.*


/**
 * Interpret logical formulas with fuzzy sets.
 */
object Semantics:

  /**
   * An environment maps predicate names to fuzzy sets of `A`s.
   */
  type Env[A] = Map[String, Set[A]]

  /**
   * Interpret a formula in a given environment.
   *
   * @param f A formula to interpret
   * @param env An environment
   * @param logic A family of fuzzy logic operations
   * @return A fuzzy set (the "interpretation" of the given formula)
   *
   * @throws NoSuchElementException
   *         If a predicate is missing from the given environment.
   */
  def interpret[A](f: Formula, env: Env[A], logic: Logic[A]): Set[A] =
    f match
      case Formula.Atom(name) =>
        env.getOrElse(name,
          throw new NoSuchElementException(s"Unknown predicate '$name'")
        )

      case Formula.Not(p) =>
        val new_p = interpret(p, env, logic)
        Set.complement(new_p)

      case Formula.And(p, q) =>
        val new_p = interpret(p, env, logic)
        val new_q = interpret(q, env, logic)
        logic.and(new_p, new_q)

      case Formula.Or(p, q) =>
        val new_p = interpret(p, env, logic)
        val new_q = interpret(q, env, logic)
        logic.or(new_p, new_q)

      case Formula.Implies(p, q) =>
        val new_p = interpret(p, env, logic)
        val new_q = interpret(q, env, logic)
        logic.implies(new_p, new_q)