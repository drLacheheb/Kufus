# Keep entry point and preserve stack trace / annotation / Kotlin metadata
-keep class io.github.drlacheheb.kufus.MainKt {
    public static void main(java.lang.String[]);
}

-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,SourceFile,LineNumberTable,Record,PermittedSubclasses
-keep class kotlin.Metadata { *; }

# Keep kotlinx.serialization generated serializers and worker IPC models
-dontnote kotlinx.serialization.AnnotationsKt
-keep class io.github.drlacheheb.kufus.disk.model.** { *; }
-keep,includedescriptorclasses class io.github.drlacheheb.kufus.**$$serializer { *; }
-keepclassmembers class io.github.drlacheheb.kufus.** {
    *** Companion;
}
-keepclasseswithmembers class io.github.drlacheheb.kufus.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep kotlinx.coroutines Swing dispatcher & ServiceLoader providers
-keep class kotlinx.coroutines.swing.SwingDispatcherFactory { *; }
-keep class * implements kotlinx.coroutines.internal.MainDispatcherFactory { *; }
-keep class * implements kotlinx.coroutines.CoroutineExceptionHandler { *; }
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Keep Skiko / Skia JNI bindings and Compose AWT integration
-keep class org.jetbrains.skia.** { *; }
-keep class org.jetbrains.skiko.** { *; }
-keep class androidx.compose.ui.awt.** { *; }
-keep class androidx.compose.ui.platform.** { *; }
-keep class io.github.drlacheheb.kufus.resources.** { *; }

# Keep Koin DI and Decompose / Essenty lifecycle state management
-keep class org.koin.** { *; }
-keep class com.arkivanov.decompose.** { *; }
-keep class com.arkivanov.essenty.** { *; }

# Suppress warnings from optional platform/JVM/Android references in multiplatform dependencies
-dontwarn sun.misc.Unsafe
-dontwarn jdk.internal.**
-dontwarn java.lang.invoke.**
-dontwarn android.**
-dontwarn androidx.annotation.**
-dontwarn com.jetbrains.**
-dontwarn org.jetbrains.skiko.**
-dontwarn org.jetbrains.skia.**
-dontwarn org.slf4j.**
-dontwarn kotlinx.atomicfu.**
-dontwarn kotlin.concurrent.atomics.**
-dontwarn kotlin.reflect.**
-dontwarn com.apple.**
-dontwarn apple.**
