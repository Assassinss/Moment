# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# rx
-dontwarn rx.**
-keep class rx.**{*;}
-keepclassmembers class rx.** { *; }

-keep class me.zsj.moment.**{*;}
-keepclasseswithmembernames class * {
    native <methods>;
}

-keep public class me.zsj.moment.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepattributes *Annotation*
-keep class com.squareup.okhttp3.** { *; }
-dontwarn okio.**
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn com.squareup.okhttp3.**

#jsoup
-keep class org.jsoup.** {*;}
-keepclassmembers class rx.** { *; }

#umeng
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

# bughd
-keepattributes Exceptions, Signature, LineNumberTable

-dontwarn java.lang.invoke**
