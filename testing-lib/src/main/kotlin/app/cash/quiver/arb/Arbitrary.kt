package app.cash.quiver.arb

import app.cash.quiver.Outcome
import app.cash.quiver.toOutcome
import arrow.core.Ior
import arrow.core.leftIor
import arrow.core.rightIor
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.map
import io.kotest.property.arrow.core.either
import io.kotest.property.arrow.core.option

fun <E, A> Arb.Companion.outcome(error: Arb<E>, value: Arb<A>): Arb<Outcome<E, A>> =
  Arb.either(error, Arb.option(value)).map { it.toOutcome() }

fun <E, A> Arb.Companion.ior(error: Arb<E>, value: Arb<A>): Arb<Ior<E, A>> =
  Arb.choice(
    error.map { it.leftIor() },
    value.map { it.rightIor() },
    Arb.bind(error, value) { e, a -> Ior.Both(e, a) }
  )
