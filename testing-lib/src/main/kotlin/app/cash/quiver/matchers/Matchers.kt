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

/**
 * Assert that the Result is successful and return its unwrapped value.
 *
 * The primary advantage over Kotest's `shouldBeSuccess` is that this matcher bubbles up
 * the original exception when the Result is a failure, rather than wrapping it in an
 * AssertionError. This preserves the full stack trace and error context, making test
 * failures easier to debug.
 *
 * Additionally, this matcher returns the unwrapped value, allowing you to use it in
 * subsequent assertions or operations without needing to call `getOrNull()` or
 * `getOrThrow()` separately.
 *
 * Example:
 * ```
 * val userId = getUserResult().shouldBeSuccessful()
 * userId shouldBe 42
 * ```
 *
 * @return The unwrapped success value
 * @throws Throwable if the Result is a failure, the original exception is thrown (not wrapped)
 */
fun <T> Result<T>.shouldBeSuccessful(): T = this.getOrThrow()

/**
 * Assert that the Result is successful and run assertions on its unwrapped value.
 *
 * This overload is useful when you want to make multiple assertions on the success
 * value without needing to unwrap it first. The value is also returned for further use.
 *
 * Example:
 * ```
 * Result.success(User(id = 1, name = "Alice")).shouldBeSuccessful { user ->
 *   user.id shouldBe 1
 *   user.name shouldBe "Alice"
 * }
 * ```
 *
 * @param block A lambda that receives the unwrapped value and can perform assertions on it
 * @return The unwrapped success value
 * @throws Throwable if the Result is a failure, the original exception is thrown
 */
inline fun <T> Result<T>.shouldBeSuccessful(block: (T) -> Unit): T = this.getOrThrow().also(block)

/**
 * Assert that the Result is successful and equals the expected value.
 *
 * Similar to Kotest's `shouldBeSuccess`, but returns the unwrapped value for further use.
 * This enables fluent assertion chains.
 *
 * Example:
 * ```
 * val name = getUserName() shouldBeSuccessful "Alice"
 * // `name` now contains "Alice" and can be used in subsequent code
 * ```
 *
 * @param expected The expected success value
 * @return The unwrapped success value
 * @throws AssertionError if the values don't match
 * @throws Throwable if the Result is a failure, the original exception is thrown
 */
infix fun <T> Result<T>.shouldBeSuccessful(expected: T): T = this.getOrThrow().also { it.shouldBe(expected) }
