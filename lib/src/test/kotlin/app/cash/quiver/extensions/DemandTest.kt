package app.cash.quiver.extensions;

import ValueIsNull
import arrow.core.None
import arrow.core.Some
import demand
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess

class DemandTest : StringSpec({
  "demand is Success when value is not null" {
    val foo: String? = "bar"
    foo.demand("label").shouldBeSuccess("bar")
  }

  "demand is Success when value is Some" {
    Some("bar").demand("label").shouldBeSuccess("bar")
  }

  "demand is Failure when value is null" {
    val foo: String? = null
    foo.demand("label").shouldBeFailure<ValueIsNull>()
  }

  "demand is Failure when value is None" {
    None.demand("label").shouldBeFailure<ValueIsNull>()
  }
})
