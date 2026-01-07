package app.cash.quiver.extensions

import app.cash.quiver.matchers.shouldBeInvalid
import app.cash.quiver.matchers.shouldBeValid
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ValidatedTest : StringSpec({
  "validNel creates an Either.Right<A>" {
    "hello world".validNel().shouldBeValid() shouldBe "hello world"
  }

  "invalidNel creates an Either.Left<NonEmptyList<E>>" {
    "uh oh!".invalidNel().shouldBeInvalid() shouldBe setOf("uh oh!")
  }
})
