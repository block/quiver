# Change Log

## Unreleased
* Upgrades Arrow to 2.x (Hugo Müller-Downing)
* Removes `OutcomeEffectScope` and `OutcomeEagerEffectScope` in favour of `OutcomeBuilder` (Hugo Müller-Downing)
* Removes methods interacting with `Validated`, as `Validated` is now deprecated (Hugo Müller-Downing)
* Replaces `ValidatedNel` with a typealias of `Either<NonEmptyList<E>, A>` (Hugo Müller-Downing)

## [0.5.12]
* Adds `<A, B> Iterable<A>.traverse(f: (A) -> Result<B>): Result<List<B>>` (Chris Myers)
* Adds `<A, B> Iterable<A>.traverseResult(f: (A) -> Result<B>): Result<List<B>>` (Chris Myers)

## [0.5.11]
* Adds `outcomeOf{}` Raise DSL which allows for greater interoperability with Result<Option<A>> and ErrorOr<A> (Chris Myers)

## [0.5.10]
* Adds `<A> OutcomeOf<A>.asResult(): Result<Option<A>>` (Chris Myers)
* Adds `<T> Result<Option<T>>.toOutcomeOf(): OutcomeOf<T>` (Chris Myers)
* Adds `<A> Arb.Companion.result(error: Throwable, value: Arb<A>): Arb<Result<A>>` (Chris Myers)
* Adds `Arb.Companion.outcomeOf(error: Throwable, value: Arb<A>): Arb<OutcomeOf<A>>` (Chris Myers)

## [0.5.9]

### Added - 2024-08-05
* Adds `Either<A, B>.handleErrorWith(f: (A) -> Either<C, B>): Either<C, B>` (Chris Myers)

## [0.5.8]

### Added - 2024-07-31
* Adds `Result<T>.handleFailureWith(f: (Throwable) -> Result<T>): Result<T>` (Chris Myers)
* Adds `<T> Result<T>.toOutcome(): OutcomeOf<T>` (Chris Myers)
* Adds `<A, B: Throwable> Option<A>.toResult(error: () -> B): Result<A>` (Chris Myers)

## [0.5.7] - 2024-06-29

### Added
* Adds `Result<T>.isSuccess(predicate: (T) -> Boolean): Boolean` and `Result<T>.isFailure(predicate: (Throwable) -> Boolean): Boolean` (Jem Mawson)

## [0.5.6] - 2024-06-21

### Added
* Adds `Result<T>.unit(): Result<Unit>` as alias for `.map { }` (Jem Mawson)
* Adds `Result<T>.tap` and `Result<T>.flatTap` (Jem Mawson)
* Adds `Result<T>.toOutcome` (Jem Mawson)

## [0.5.5] - 2024-06-20

### Added
* Adds `Result.catch` to enable the same behaviour as `Either.catch` (Alejandro Metke)
* Adds `Result<Result<T>>.flatten(): Result<T>` (Jem Mawson)

### Fixed
* `T.failure(): Result<T>` was invalid. Changed to `Throwable.failure(): Result<A>` (Jem Mawson)

## [0.5.4] - 2024-05-21

### Added
* Adds `T.success()` as a shorthand for creating a success `Result` (Jem Mawson)
* Adds `<T : Throwable>.failure()` as a shorthand for creating a failure `Result` (Jem Mawson)
* Adds `T.toResult()` as a shorthand for converting nullable types to `Result<T>` (Alejandro Metke)
* Adds `Result.mapFailure()` to enable mapping the failure in a result to a different `Throwable` (Alejandro Metke)
* Backport traverse functions on Either, Iterable and Option (Andrew Parker)
* Backport traverse functions on Sequence, Map and Ior (Andrew Parker)

## [0.5.3] - 2024-05-09

### Added
* Adds `ErrorOr.toResult()` as a shorthand for converting `Either<Throwable, T>` to `Result<T>` (Jem Mawson)

## [0.5.2] - 2024-04-08

### Changed
Upgraded Arrow to v1.2.4

## [0.5.1] - 2024-03-26
### Added
* Adds `Option.ifPresent()` as alternative to `Option.forEach()` for symmetry with `Option.ifAbsent()` (Mia Balogh)

## [0.5.0] - 2023-08-26

### Added
* Lazy version of `or`:`Option.or(() -> Option<T>)` (Chris Myers)
* Adds `Option.orEmpty()` (Milly Rowett)
* Backport traverse functions on NonEmptyList (Andrew Parker)

### Changed
* Deprecated `Option.or(Option)` in favour of `Option.or(() -> Option<T>)` (Chris Myers)

## [0.4.0] - 2023-07-26

### Added
* `Option.or(Option)` (Mehdi Mollaverdi)

### Changed
* Bumped Arrow to v1.2.0 from v1.2.0-RC (Jem Mawson)


## [0.3.0] - 2023-06-16

### Added
* `kotlin.Result.toEither()` (Jem Mawson)
* `validateNotNull` extension function on nullable values (Hugo Müller-Downing)


## [0.2.0] - 2023-04-06

### Added
* Add Either.traverse (Simon Vergauwen)
* Either, Nullable & Ior zip (Simon Vergauwen)
* Adds validateNotNull extension function on nullable values (Hugo Müller-Downing)

### Fixed
* Fix outcome traverse (Simon Vergauwen)
* Fix nested nullable issue (Simon Vergauwen)
* Fix nested null bug (#22) (Simon Vergauwen)


## [0.1.0] - 2023-03-06

### Added
* `Option.unit()` and `Either.unit()` to replace the `void()` method deprecated by Arrow.
* `withRetries` method on suspended supplier functions to provide opinionated access to Arrow's `Schedule`.
