
![Quiver logo](./images/quiver-logo-01.svg#gh-light-mode-only)
![Quiver logo](./images/quiver-logo-02.svg#gh-dark-mode-only)

Quiver is a library that builds upon [Arrow](https://arrow-kt.io/) to make functional programming in Kotlin even 
more accessible & delightful.

[<img src="https://img.shields.io/maven-central/v/app.cash.quiver/lib?label=latest%20release"/>](https://central.sonatype.com/namespace/app.cash.quiver)

Main features include:
* `Outcome` type for modelling the presence, absence or error state of a value.
* Extension methods on `Option`, `List`, `Validated` and `Either` for improved error handling, combinators and interoperability with other Arrow and Kotlin types.
* Extension methods on suspended functions to allow for seamless retries of operations.

There are two published libraries.

* `lib` contains types and functions to provide additional functional programming idioms over and above what is available from Arrow.
* `lib-test` contains kotest Matchers and Arbs for writing tests that use types found in `lib`.

## Getting Started

On the [Sonatype page for Quiver](https://central.sonatype.com/namespace/app.cash.quiver), choose the latest version 
of `lib` (and `lib-test` if desired) and follow the instructions for inclusion in your build tool. 

Quiver is a Kotlin Multiplatform library. Gradle resolves the right artifact for each target from `app.cash.quiver:lib`;
JVM, JS, iOS, macOS, tvOS, watchOS, Linux (x64) and Windows (mingw x64) are supported. Other build tools should
depend on the platform artifact directly, for example `app.cash.quiver:lib-jvm`.

Quiver requires Kotlin 2.2 or newer. `lib-test` requires Kotest 6.2.6 or newer.

## Building

Install Hermit, see instructions at https://cashapp.github.io/hermit/

Use gradle to run all Kotlin tests locally:

```shell
gradle build
```

## Documentation

The API documentation is published at https://block.github.io/quiver/. It is rebuilt from `main` on every merge, so it
may include changes that have not been released yet.

## Changelog

See a list of changes in each release in the [CHANGELOG](CHANGELOG.md).

## Contributing

For details on contributing, see the [CONTRIBUTING](CONTRIBUTING.md) guide.
