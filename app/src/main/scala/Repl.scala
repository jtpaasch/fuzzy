package app

import scala.io.StdIn.readLine

import fuzzy.Core.{Set, Real, Logic}
import fuzzy.Set
import fuzzy.Formula
import fuzzy.Semantics
import fuzzy.Parser

object Repl:

  private val godel = new Set.Godel[Real]()
  private val goguen = new Set.Goguen[Real]()
  private val lukasiewicz = new Set.Lukasiewicz[Real]()

  private val logics: Map[String, Logic[Real]] = Map(
    "Godel" -> godel,
    "Goguen" -> goguen,
    "Lukasiewicz" -> lukasiewicz
  )

  private def repl(env: Semantics.Env[Real], logic: Logic[Real]): Unit =
    var continue = true
    var activeLogic = logic

    while continue do
      val input = readLine("fuzzy> ").trim

      input match
        case ":help" =>
          println("Usage:")
          println("- Input a formula, e.g. '(Cold && !Hot) -> Warm',")
          println("  using predicates 'Cold', 'Warm', and 'Hot'.")
          println("- The REPL will then print the degree to which various")
          println("  temperatures satisfy the given formula.")
          println("Commands:")
          println("- ':logic Godel|Goguen|Lukasiewicz' to switch logics.")
          println("- ':quit' to exit.")
        case ":quit" =>
          println("Goodbye")
          continue = false
        case _ if input.startsWith(":logic ") =>
          val name = input.stripPrefix(":logic ").trim
          logics.get(name) match
            case Some(newLogic) =>
              activeLogic = newLogic
              println(s"Switched logic to '$name'")
            case None =>
              println(s"Unknown logic: '$name'")
              println(s"Try :help")
        case other =>
          Parser.parse(other) match
            case Left(err) =>
              println(s"Parser error: $err.msg")
            case Right(formula) =>
              val result: Set[Real] = Semantics.interpret(
                formula, env, activeLogic
              )
              println(s"Parsed formula: $formula")
              val temps = List(
                0.0, 15.0, 32.0, 45.0, 60.0, 75.0, 85.0, 95.0, 110.0
              )
              println("Temp\tResult")
              for temp <- temps do
                val r = result(temp)
                println(f"$temp%.2f\t$r%.2f")

  def run: Unit =
    val cold: Set[Real] = Set.triangular(0.0, 32.0, 50.0)
    val warm: Set[Real] = Set.triangular(60.0, 75.0, 90.0)
    val hot: Set[Real] = Set.triangular(80.0, 95.0, 110.0)

    val env: Semantics.Env[Real] = Map(
      "Cold" -> cold,
      "Warm" -> warm,
      "Hot" -> hot
    )

    val logic: Logic[Real] = new Set.Godel[Real]()

    println("Fuzzy Logic REPL (type ':help' for help)")
    repl(env, logic)