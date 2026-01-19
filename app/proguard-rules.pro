
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

-keep class com.google.ads.mediation.** { *; }
-keep class com.google.android.gms.ads.mediation.** { *; }
-dontwarn com.google.ads.mediation.**

-keep class androidx.** { *; }
-dontwarn androidx.**

-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-dontwarn kotlinx.**

-keep class * implements androidx.viewbinding.ViewBinding { *; }
-keepclassmembers class **Binding { *; }


-keep class androidx.databinding.** { *; }
-dontwarn androidx.databinding.**

-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

-keep class com.bumptech.glide.** { *; }
-dontwarn com.bumptech.glide.**

-keep class com.example.** { *; }

-keepclassmembers enum * { *; }

-keepattributes *Annotation*
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
