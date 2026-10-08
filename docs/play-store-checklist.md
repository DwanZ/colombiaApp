# Play Store publish checklist

## Technical (done in repo)

- [x] Wear OS / Health Connect removed
- [x] `targetSdk = 35`, `minSdk = 31`, `compileSdk = 37`
- [x] Release R8 minify + resource shrink enabled
- [x] Optional `keystore.properties` signing wiring
- [x] Privacy policy draft (`docs/privacy-policy.md`)
- [x] ProGuard keep rules for Retrofit / Gson / MapLibre

## Console (manual)

1. Create app in Google Play Console (`com.dwan.colombia`)
2. Generate upload keystore; fill `keystore.properties` (never commit)
3. Host privacy policy publicly; paste URL in Console
4. Complete Data safety:
   - Collected: none personally identifying
   - Data shared: none
   - Network: loads public API + map tiles
5. Content rating questionnaire
6. Upload phone screenshots + feature graphic
7. Short description (~80 chars) / full description
8. `./gradlew :app:bundleRelease` → upload AAB
9. Internal testing track → production rollout
