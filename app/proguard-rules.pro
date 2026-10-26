# Conservar atributos de nombres de archivos para depuración interna si fuera necesario, pero ocultando detalles innecesarios
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Remover llamadas a los logs del sistema (Log.d, Log.v, etc.) en versiones de producción para evitar fugas de información
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int d(...);
}

# Reglas generales para mantener las clases de Google Maps y seguridad sin alterar sus nombres esenciales
-keep class com.google.android.gms.maps.** { *; }
-dontwarn com.google.android.gms.maps.**

-keep class androidx.security.crypto.** { *; }
-dontwarn androidx.security.crypto.**