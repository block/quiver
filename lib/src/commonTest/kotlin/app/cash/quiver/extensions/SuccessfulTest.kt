package app.cash.quiver.extensions

import app.cash.quiver.matchers.shouldBeSuccessful
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SuccessfulTest : StringSpec({
  "shouldBeSuccessful succeeds on success" {
    Result.success().shouldBeSuccessful()
  }

  "shouldBeSuccessful bubbles up exception on failure" {
    val exception = IllegalStateException("oh no!")

    shouldThrow<IllegalStateException> {
      Result.failure<Throwable>(exception).shouldBeSuccessful()
    }.message shouldBe "oh no!"
  }

  "shouldBeSuccessful with block runs assertions on success value" {
    var blockRan = false
    val result = Result.success(42).shouldBeSuccessful { value ->
      blockRan = true
      value shouldBe 42
    }
    result shouldBe 42
    blockRan shouldBe true
  }

  "shouldBeSuccessful with block bubbles up exception on failure" {
    val exception = IllegalStateException("failure!")

    shouldThrow<IllegalStateException> {
      Result.failure<Int>(exception).shouldBeSuccessful { value ->
        value shouldBe 42
      }
    }.message shouldBe "failure!"
  }

  "shouldBeSuccessful with expected value succeeds when values match" {
    val result = Result.success("hello") shouldBeSuccessful "hello"
    result shouldBe "hello"
  }

  "shouldBeSuccessful with expected value fails when values don't match" {
    shouldThrow<AssertionError> {
      Result.success("hello") shouldBeSuccessful "world"
    }
  }

  "shouldBeSuccessful with expected value bubbles up exception on failure" {
    val exception = IllegalStateException("failed!")

    shouldThrow<IllegalStateException> {
      Result.failure<String>(exception) shouldBeSuccessful "expected"
    }.message shouldBe "failed!"
  }
})
