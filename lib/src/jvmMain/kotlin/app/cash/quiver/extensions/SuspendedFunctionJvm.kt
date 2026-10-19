@file:JvmMultifileClass
@file:JvmName("SuspendedFunctionKt")

package app.cash.quiver.extensions

import java.time.Duration
import kotlin.time.toKotlinDuration

/**
 * Binary compatibility for callers compiled against the `java.time.Duration` signatures that existed before
 * Quiver became multiplatform. Source callers should pass a [kotlin.time.Duration]. The multifile class keeps these in
 * the same `SuspendedFunctionKt` facade that older callers link against.
 */
@Deprecated("Pass a kotlin.time.Duration instead.", level = DeprecationLevel.HIDDEN)
suspend fun <T> (suspend () -> T).withRetriesOrThrow(
  until: (ErrorOr<T>) -> Boolean = { it.isRight() },
  additionalTimes: Int = 4,
  delay: Duration = Duration.ofMillis(20L),
  exponentialBackoff: Boolean = false,
  jitter: Boolean = false,
): ErrorOr<T> = withRetriesOrThrow(until, additionalTimes, delay.toKotlinDuration(), exponentialBackoff, jitter)

@Deprecated("Pass a kotlin.time.Duration instead.", level = DeprecationLevel.HIDDEN)
suspend fun <T> (suspend () -> ErrorOr<T>).withRetries(
  until: (ErrorOr<T>) -> Boolean = { it.isRight() },
  additionalTimes: Int = 4,
  delay: Duration = Duration.ofMillis(20L),
  exponentialBackoff: Boolean = false,
  jitter: Boolean = false,
): ErrorOr<T> = withRetries(until, additionalTimes, delay.toKotlinDuration(), exponentialBackoff, jitter)
