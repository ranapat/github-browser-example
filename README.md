# Architectural components android example github browser

Github browser based on architectural components - MVVM, ViewModel, RX 2.x, Room implemented in Java and Kotlin

## Requirements
* Java 8
* Android SDK
* Gradle

## Building

Build tool is gradle

### Assemble
Run `./gradlew assemble`

### Run unit tests
Run `./gradlew test`

### Run ui tests
Run `./gradlew connectedCheck`

### Run lint
Run `./gradlew lint`

### Run all development tests
This will run lintDevelopmentDebug, testDevelopmentDebugUnitTestCoverage and connectedDevelopmentDebugAndroidTest

You can find the outputs here:
- for the lint `./app/build/reports/lint-results-developmentDebug.html`
- for the unit test coverage `./app/build/reports/jacoco/testDevelopmentDebugUnitTestCoverage/html/index.html`
- for the unit test summary `./app/build/reports/tests/testDevelopmentDebugUnitTest/index.html`
- for the connected tests summary `./app/build/reports/androidTests/connected/flavors/DEVELOPMENT/index.html`

Run `./gradlew devTests`
