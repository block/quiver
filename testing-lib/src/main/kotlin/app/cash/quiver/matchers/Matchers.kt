package app.cash.quiver.matchers

import app.cash.quiver.Absent
import app.cash.quiver.Failure
import app.cash.quiver.Outcome
import app.cash.quiver.Present
import app.cash.quiver.extensions.ValidatedNel
import arrow.core.Either
import arrow.core.NonEmptyList
import io.kotest.matchers.shouldBe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun <A, B> Outcome<A, B>.shouldBePresent(failureMessage: (A) -> String = { "Expected Outcome.Present, but found $it" }): B {
  contract {
    returns() implies (this@shouldBePresent is Present<B>)
  }
  return when (this) {
    Absent -> throw AssertionError("Expecting Present, got Absent")
    is Failure -> throw AssertionError(failureMessage(error))
    is Present -> value
  }
}

@OptIn(ExperimentalContracts::class)
fun <A, B> Outcome<A, B>.shouldBeAbsent(failureMessage: (A) -> String = { "Expected Outcome.Absent, but found $it" }) {
  contract {
    returns() implies (this@shouldBeAbsent is Absent)
  }
  return when (this) {
    Absent -> Unit
    is Failure -> throw AssertionError(failureMessage(error))
    is Present -> throw AssertionError("Got present, expected Absent ($this)")
  }
}

@OptIn(ExperimentalContracts::class)
fun <A, B> Outcome<A, B>.shouldBeFailure(): A {
  contract {
    returns() implies (this@shouldBeFailure is Failure<A>)
  }
  return when (this) {
    Absent -> throw AssertionError("Expected Outcome.Failure but got Absent")
    is Failure -> error
    is Present -> throw AssertionError("Expected Outcome.Failure but got Present($value)")
  }
}

@OptIn(ExperimentalContracts::class)
inline fun <E, reified A> ValidatedNel<E, A>.shouldBeValid(): A {
  contract {
    returns() implies (this@shouldBeValid is A)
  }

  return when(this) {
    is Either.Left -> throw AssertionError("Expected Right (Valid), but found $this")
    is Either.Right -> value
  }
}
@OptIn(ExperimentalContracts::class)
fun <E, A> ValidatedNel<E, A>.shouldBeInvalid(): Set<E> {
  contract {
    returns() implies (this@shouldBeInvalid is Set<*>)
  }

  return when(this) {
    is Either.Left -> value.toSet()
    is Either.Right -> throw AssertionError("Expected Left (Invalid), but found $this")
  }
}

/** Assert that the Result is successful and return its value. */
fun <T> Result<T>.shouldBeSuccessful(): T = this.getOrThrow()

/** Assert that the Result is successful and run assertions on its value. */
inline fun <T> Result<T>.shouldBeSuccessful(block: (T) -> Unit): T = this.getOrThrow().also(block)

/** Assert that the Result is successful and equals the expected value. */
infix fun <T> Result<T>.shouldBeSuccessful(expected: T): T = this.getOrThrow().also { it.shouldBe(expected) }
