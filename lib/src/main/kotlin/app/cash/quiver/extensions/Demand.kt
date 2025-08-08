import app.cash.quiver.extensions.toResult
import arrow.core.Option

/**
 * Ensures that the nullable receiver is not null. Converts the receiver to a successful [Result]
 * if not null or to a failure containing a [FieldIsNull] exception if null.
 *
 * Example:
 * ```kotlin
 * // When the receiver is not null
 * val foo: String? = "bar"
 * foo.demand("foo") // Result.Success("foo")
 *
 * // When the receiver is null
 * val foo: String? = null
 * foo.demand("foo") // Result.Failure(FieldIsNull("Field foo is null"))
 * ```
 *
 * @param field the name of the field being validated. Used to generate a meaningful error message if the field is null.
 * @return a [Result] containing the receiver value if not null, or a failure with a [FieldIsNull] exception if the receiver is null.
 */
fun <A> A?.demand(field: String): Result<A> =
  this.toResult { FieldIsNull("Field $field is null") }

/**
 * Ensures an Option is Some, wrapping the inner value in a `Result`.
 * If the Option is empty, the operation will fail, associating the failure with the provided field name.
 *
 * @param field The name of the field to associate with the failure if the Option is empty.
 * @return A Result containing the value inside the Option if present, or a failure associated with the given field if empty.
 */
fun <A> Option<A>.demand(field: String): Result<A> = this.getOrNull().demand(field)

data class FieldIsNull(override val message: String?) : Throwable(message)
