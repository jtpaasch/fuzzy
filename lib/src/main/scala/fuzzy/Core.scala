package fuzzy

/**
 * Common type aliases, traits, etc.
 */
object Core:

  /**
   * Real numbers are used for membership degrees (range [0, 1]).
   */
  type Real = Double

  /**
   * A fuzzy set of `A`s is a function: `μ : A → [0,1]`.
   */
  type Set[A] = A => Real

  /**
   * A logic implements the basic logical operations:
   * - t-norm (and)
   * - t-conorm (or)
   * - implication
   */
  trait Logic[A]:
    def and(f: Set[A], g: Set[A]): Set[A]
    def or(f: Set[A], g: Set[A]): Set[A]
    def implies(f: Set[A], g: Set[A]): Set[A]