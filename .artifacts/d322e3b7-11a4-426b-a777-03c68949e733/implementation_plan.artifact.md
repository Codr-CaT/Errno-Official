# ERNO Android Foundation Implementation Plan

This plan outlines the steps to rename the application package to `com.erno.app`, set up the MVVM architecture foundation, and implement a clean initial home screen.

## User Review Required

> [!IMPORTANT]
> The package name will be changed from `com.example.app` to `com.erno.app`. This affects the `applicationId`, `namespace`, and all source code package declarations.

## Proposed Changes

### 1. Build Configuration [MODIFY]
- Update `app/build.gradle.kts`:
    - Change `namespace` to `"com.erno.app"`.
    - Change `applicationId` to `"com.erno.app"`.
    - Add `Navigation Compose` and `Lifecycle ViewModel Compose` dependencies if missing.

### 2. Package Renaming & Architecture Setup [NEW/MODIFY]
- Create the new directory structure: `com/erno/app/data`, `com/erno/app/ui`, `com/erno/app/navigation`.
- Move and update existing files:
    - [MODIFY] [MainActivity.kt](file:///C:/Users/Mayank/AndroidStudioProjects/MyApplication/app/src/main/java/com/erno/app/MainActivity.kt)
    - [MODIFY] [Theme.kt](file:///C:/Users/Mayank/AndroidStudioProjects/MyApplication/app/src/main/java/com/erno/app/ui/theme/Theme.kt)
    - [MODIFY] [Color.kt](file:///C:/Users/Mayank/AndroidStudioProjects/MyApplication/app/src/main/java/com/erno/app/ui/theme/Color.kt)
    - [MODIFY] [Type.kt](file:///C:/Users/Mayank/AndroidStudioProjects/MyApplication/app/src/main/java/com/erno/app/ui/theme/Type.kt)
- Update [AndroidManifest.xml](file:///C:/Users/Mayank/AndroidStudioProjects/MyApplication/app/src/main/AndroidManifest.xml) with the new package references.

### 3. New Architecture Components [NEW]
#### Data Layer
- [NEW] `data/model/PredictionResult.kt`: Basic data model.
- [NEW] `data/remote/ErnoApiService.kt`: API interface placeholder.
- [NEW] `data/repository/AnalysisRepository.kt`: Repository interface and implementation.

#### UI Layer
- [NEW] `ui/screens/home/HomeScreen.kt`: Initial home screen.
- [NEW] `ui/screens/home/HomeViewModel.kt`: ViewModel for the home screen.
- [NEW] `ui/components/ErnoTopBar.kt`: Reusable top bar component.

#### Navigation Layer
- [NEW] `navigation/NavGraph.kt`: Navigation logic.
- [NEW] `navigation/Screen.kt`: Navigation routes.

### 4. Testing Packages [MODIFY]
- Rename packages in `src/test` and `src/androidTest`.

## Verification Plan

### Automated Tests
- Run `gradlew assembleDebug` to verify the project builds.
- Run `gradlew test` to verify unit tests.

### Manual Verification
- Deploy to an emulator/device and verify the ERNO home screen is displayed.
- Check that navigation (if any added) works correctly.
