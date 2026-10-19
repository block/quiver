package app.cash.quiver.extensions

import arrow.core.Option

/**
 * Ensures that the nullable receiver is not null. Converts the receiver to a successful [Result]
 * if not null or to a failure containing a [ValueIsNull] exception if null.
 *
 * Example:
 * ```kotlin
 * // When the receiver is not null
 * val foo: String? = "bar"
 * foo.demand("foo") // Result.Success("foo")
 *
 * // When the receiver is null
 * val foo: String? = null
 * foo.demand("foo") // Result.Failure(ValueIsNull("Value foo is null"))
 * ```
 *
 * @param label the name of the value being validated. Used to generate a meaningful error message if the value is null.
 * @return a [Result] containing the receiver value if not null, or a failure with a [ValueIsNull] exception if the receiver is null.
 */
fun <A> A?.demand(label: String): Result<A> =
  this.toResult { ValueIsNull("Value $label is null") }

/**
 * Ensures an Option is Some, wrapping the inner value in a `Result`.
 * If the Option is empty, the operation will fail, associating the failure with the provided label name.
 *
 * @param label The name of the value to associate with the failure if the Option is empty.
 * @return A Result containing the value inside the Option if present, or a failure associated with the given value if empty.
 */
fun <A> Option<A>.demand(label: String): Result<A> = this.getOrNull().demand(label)

data class ValueIsNull(override val message: String?) : Throwable(message)
