package app.cash.quiver.extensions

import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.time.Duration.Companion.milliseconds

class RetryTest : StringSpec({

  val runtimeException = RuntimeException("bad")

  "no retries when the effect is successful" {
    val effectTester = EffectTester(true.right())
    val result = effectTester.effect.withRetries(until = { it.isRight() })
    result shouldBeRight true
    effectTester.callCount() shouldBe 1
  }

  "default until function should check the right side of the either when retrying" {
    val effectTester = EffectTester(true.right())
    val result = effectTester.effect.withRetries()
    result shouldBeRight true
    effectTester.callCount() shouldBe 1
  }

  "one retry when the first call is unsuccessful" {
    val effectTester = EffectTester(listOf(false, true, true).map { { it.right() } })
    val result = effectTester.effect.withRetries(
      until = { it.getOrElse { false } },
      delay = 1.milliseconds
    )
    result shouldBeRight true
    effectTester.callCount() shouldBe 2
  }

  "exhaust retrying when no call is successful" {
    val effectTester = EffectTester(false.right())
    val result = effectTester.effect.withRetries(
      until = { it.getOrElse { false } },
      additionalTimes = 7,
      delay = 1.milliseconds
    )
    result shouldBeRight false
    effectTester.callCount() shouldBe 8
  }

  "retry on error until success" {
    val effectTester = EffectTester(listOf({ runtimeException.left() }, { true.right() }))
    val result = effectTester.effect.withRetries(
      until = { it.isRight() },
      delay = 1.milliseconds
    ).orThrow()
    result shouldBe true
    effectTester.callCount() shouldBe 2
  }

  "retry on error until exhausted" {
    val effectTester = EffectTester<ErrorOr<Int>>(listOf { runtimeException.left() })
    shouldThrow<RuntimeException> {
      effectTester.effect.withRetries(
        until = { it.isRight() },
        additionalTimes = 3,
        delay = 1.milliseconds
      ).orThrow()
    }
    effectTester.callCount() shouldBe 4
  }

  "be able to ignore expected errors when retrying" {
    val effectTester = EffectTester<ErrorOr<Int>>(listOf { runtimeException.left() })
    shouldThrow<RuntimeException> {
      effectTester.effect.withRetries(
        until = { it.isLeft() },
        delay = 1.milliseconds
      ).orThrow()
    }
    effectTester.callCount() shouldBe 1
  }

})
