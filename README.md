# Fuzzy logics

A little fuzzy logic engine, for pedagogical purposes.
To run it, you need Scala 3 (sbt).

Start the REPL:

    sbt app/run

Try help:

    fuzzy> :help

Try a formula:

    fuzzy> (Cold && !Hot) -> Warm

Switch the logic:

    fuzzy> :logic Lukasiewicz

Try the formula again:

    fuzzy> (Cold && !Hot) -> Warm

Quit:

    fuzzy> :quit

