# Colombia App

Android app showcasing Colombian country data, presidents, touristic attractions, and an interactive department map — powered by [API Colombia](https://api-colombia.com).

## Architecture

```
:app                    Application shell, navigation, theme
:core:common            Shared UI widgets + BaseViewState
:core:domain            Models + repository contracts
:core:data              Retrofit, remote sources, repository impls (Hilt)
:feature:country        Country overview
:feature:map            MapLibre department map + attractions sheet
:feature:attractions    Attractions list/detail
:feature:presidents     Presidents list/detail
```

Stack: Kotlin 2.1, Jetpack Compose (Material 3), Hilt + KSP, Retrofit, MapLibre (OSM demotiles, no Google Maps key).

## Build

```bash
./gradlew :app:assembleDebug
```

Requirements: JDK 17+, Android SDK 35/37.

## Play Store readiness

| Item | Status |
|------|--------|
| `minSdk` 31 / `targetSdk` 35 / `compileSdk` 37 | Configured |
| Release minify + shrink resources | Enabled |
| Wear OS / Health Connect | Removed |
| Privacy policy draft | See [docs/privacy-policy.md](docs/privacy-policy.md) |
| Signing | Use local `keystore.properties` (not committed) — see below |

### Release signing

1. Create a keystore (once).
2. Add `keystore.properties` at the project root (gitignored):

```properties
storeFile=C:\\path\\to\\colombia-release.jks
storePassword=***
keyAlias=colombia
keyPassword=***
```

3. Wire into `app/build.gradle.kts` `signingConfigs` before publishing (template comments in that file if needed).

### Store listing checklist

- [ ] Short + full description (EN / optional ES)
- [ ] Feature graphic 1024×500, screenshots (phone)
- [ ] Content rating questionnaire
- [ ] Data safety form (network traffic to api-colombia.com + MapLibre tile CDN)
- [ ] Privacy policy URL hosted publicly
- [ ] `./gradlew :app:assembleRelease` smoke test on a physical device

## Map data

Department polygons are a simplified GeoJSON derived from DANE MGN (public geostatistical framework), bundled in `:feature:map` assets. Attractions load from `GET /api/v1/Department/{id}/touristicattractions`.
