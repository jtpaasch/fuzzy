package fuzzy


/**
 * Logical formulas to be interpreted by fuzzy semantics.
 */
enum Formula:

  /**
   * Atomic formula.
   *
   * @param name The identifier of a predicate in the environment.
   */
  case Atom(predicate: String)

  /**
   * Negated formula.
   *
   * @param p The formula to negate.
   */
  case Not(p: Formula)

  /**
   * Conjunction formula.
   *
   * @param left The left-hand conjunct.
   * @param right The right-hand conjunct.
   */
  case And(left: Formula, right: Formula)

  /**
   * Disjunction formula.
   *
   * @param left The left-hand disjunct.
   * @param right The right-hand disjunct.
   */
  case Or(left: Formula, right: Formula)

  /**
   * Implication formula.
   *
   * @param antec The antecedent.
   * @param conseq The consequent
   */
  case Implies(antec: Formula, conseq: Formula)
