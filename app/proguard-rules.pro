# Keep Retrofit / Gson models
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.dwan.data.source.remote.** { *; }
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# MapLibre
-keep class org.maplibre.** { *; }
-dontwarn org.maplibre.**

# Hilt / Dagger
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
