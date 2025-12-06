package fuzzy

import fuzzy.Core.*

/**
 * This provides basic fuzzy set operations:
 * - Some pre-defined membership shapes (triangular, etc)
 * - A generic complement operation
 * - Gödel, Goguen, and Łukasiewicz t-norms, t-conorms, implications
 */
object Set:

  // -------------------------------------------------------
  // Pre-defined membership shapes
  // -------------------------------------------------------

  /**
   * Creates a triangular membership function.
   *
   * @param a The left support boundary (`μ(a) = 0.0`)
   * @param b The peak of the triangle (`μ(b) = 1.0`)
   * @param c The right support boundary (`μ(c) = 0.0`)
   * @return A fuzzy set `μ : Real → Real`
   */
  def triangular(a: Real, b: Real, c: Real): Set[Real] = {
    x =>
      if x <= a || x >= c then 0.0
      else if x == b then 1.0
      else if a < x && x < b then (x - a) / (b - a)
      else (c - x) / (c - b)
  }

  // TODO: add more membership shapes


  // -------------------------------------------------------
  // Complement
  // -------------------------------------------------------

  /**
   * Fuzzy complement (Zadeh negation).
   *
   * @param f A fuzzy set.
   * @return The fuzzy set `x ↦ 1.0 - f(x)`
   */
  def complement[A](f: Set[A]): Set[A] =
    x => 1.0 - f(x)


  // -------------------------------------------------------
  // Gödel logic
  // -------------------------------------------------------

  class Godel[A] extends Logic[A]:

    /**
     * Godel t-norm: `min`
     */
    def and(f: Set[A], g: Set[A]): Set[A] =  
      x => math.min(f(x), g(x))

    /**
     * Godel t-conorm: `max`
     */
    def or(f: Set[A], g: Set[A]): Set[A] =
      x => math.max(f(x), g(x))

    /**
     * Godel implication: `a → b = 1 if a ≤ b, else b`
     */
    def implies(f: Set[A], g: Set[A]): Set[A] =
      x =>
        val a = f(x)
        val b = g(x)
        if a <= b then 1.0 else b


  // -------------------------------------------------------
  // Goguen (product) logic
  // -------------------------------------------------------

  class Goguen[A] extends Logic[A]:

    /**
     * Goguen t-norm: multiplication
     */
    def and(f: Set[A], g: Set[A]): Set[A] =
      x => f(x) * g(x)

    /**
     * Goguen t-conorm: `(a + b) - (a * b)`
     */
    def or(f: Set[A], g: Set[A]): Set[A] =
      x =>
        val a = f(x)
        val b = g(x)
        (a + b) - (a * b)

    /**
     * Goguen implication: `a → b = 1 if a ≤ b else b/a`
     */
    def implies(f: Set[A], g: Set[A]): Set[A] =
      x =>
        val a = f(x)
        val b = g(x)
        if a <= b then 1.0 else b / a


  // -------------------------------------------------------
  // Łukasiewicz logic
  // -------------------------------------------------------

  class Lukasiewicz[A] extends Logic[A]:

    /**
     * Lukasiewicz t-norm: `max(0.0, (a + b) - 1.0)`
     */
    def and(f: Set[A], g: Set[A]): Set[A] =
      x =>
        val a = f(x)
        val b = g(x)
        math.max(0.0, (a + b) - 1.0)

    /**
     * Lukasiewicz t-conorm: `min(1.0, a + b)`
     */
    def or(f: Set[A], g: Set[A]): Set[A] =
      x =>
        val a = f(x)
        val b = g(x)
        math.min(1.0, a + b)

    /**
     * Lukasiewicz implication: `min(1.0, 1.0 - a + b)`
     */
    def implies(f: Set[A], g: Set[A]): Set[A] =
      x =>
        val a = f(x)
        val b = g(x)
        math.min(1.0, 1.0 - (a + b))
