# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

KUIT6_Android_API is an Android application project using Kotlin and Gradle with the Kotlin DSL.

**Package name**: `com.example.kuit6_android_api`

**Build configuration**:
- Kotlin 2.0.21
- Android Gradle Plugin 8.13.0
- Compile SDK: 36
- Min SDK: 28
- Target SDK: 36
- Java 11 (sourceCompatibility, targetCompatibility, and jvmTarget)

## Common Commands

### Build and Run
```bash
# Clean build
./gradlew clean

# Build the project
./gradlew build

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install debug APK to connected device/emulator
./gradlew installDebug
```

### Testing
```bash
# Run unit tests (in test/)
./gradlew test

# Run unit tests for debug variant
./gradlew testDebugUnitTest

# Run instrumented tests on connected device/emulator (in androidTest/)
./gradlew connectedAndroidTest

# Run specific test class
./gradlew test --tests com.example.kuit6_android_api.ExampleUnitTest

# Run specific test method
./gradlew test --tests com.example.kuit6_android_api.ExampleUnitTest.addition_isCorrect
```

### Code Quality
```bash
# Lint the project
./gradlew lint

# Generate lint report
./gradlew lintDebug
```

### Project Info
```bash
# List all tasks
./gradlew tasks

# Show project dependencies
./gradlew dependencies

# Show app module dependencies
./gradlew :app:dependencies
```

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/kuit6_android_api/  # Main source code
│   │   ├── res/                                   # Android resources
│   │   └── AndroidManifest.xml
│   ├── test/                                      # Unit tests (JVM)
│   └── androidTest/                               # Instrumented tests (Android device)
├── build.gradle.kts                               # App-level build configuration
└── proguard-rules.pro                             # ProGuard rules for release builds

build.gradle.kts                                   # Project-level build configuration
settings.gradle.kts                                # Project settings
gradle/libs.versions.toml                          # Version catalog for dependencies
```

## Dependencies

Dependencies are managed via the Gradle version catalog in `gradle/libs.versions.toml`.

Current dependencies:
- AndroidX Core KTX
- AndroidX AppCompat
- Material Components
- JUnit (unit testing)
- AndroidX Test JUnit (instrumented testing)
- Espresso (UI testing)

To add new dependencies, update `gradle/libs.versions.toml` and reference them in `app/build.gradle.kts`.

## Architecture Notes

This is a fresh Android project with minimal code. The main source directory (`app/src/main/java/com/example/kuit6_android_api/`) is currently empty. When implementing features:

- Place activities, fragments, and UI components in the main package or appropriate subpackages
- Follow standard Android architecture patterns (MVVM, MVI, etc.) as needed
- Use the existing test infrastructure for unit tests (test/) and instrumented tests (androidTest/)