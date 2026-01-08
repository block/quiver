package app.cash.quiver.extensions

import app.cash.quiver.matchers.shouldBeInvalid
import app.cash.quiver.matchers.shouldBeValid
import arrow.core.left
import arrow.core.right
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ValidatedTest : StringSpec({
  "validNel creates an Either.Right<A>" {
    "hello world".validNel().shouldBeValid() shouldBe "hello world"
  }

  "invalidNel creates an Either.Left<NonEmptyList<E>>" {
    "uh oh!".invalidNel().shouldBeInvalid() shouldBe setOf("uh oh!")
  }

  "validateEither returns Right when predicate is true" {
    "hi mum".validateEither({ it.contains("hi") }, { "where's your manners?" }) shouldBe "hi mum".right()
  }

  "validateEither returns Left when predicate is false" {
    "hello".validateEither({ it.contains("hi") }, { "where's your manners?" }) shouldBe "where's your manners?".left()
  }

  "validateResult returns Success when predicate is true" {
    "hi mum".validateResult(
      { it.contains("hi") },
      { RuntimeException("where's your manners?") }) shouldBe Result.success("hi mum")
  }

  "validateResult returns Failure when predicate is false" {
    val result = "hello".validateResult({ it.contains("hi") }, { RuntimeException("where's your manners?") })
    result.isFailure shouldBe true
    result.exceptionOrNull()?.message shouldBe "where's your manners?"
  }
})
