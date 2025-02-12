# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-keep class com.hidefiles.secure.folder.vault.clue.** {*;}
#-keep class * implements java.io.Serializable {*;}

-keep class com.hidefile.secure.folder.vault.cluecanva.* { *; }

#-dontwarn org.apache.http.**
#-dontwarn android.net.**
#-keep class org.apache.** {*;}
#-keep class org.apache.http.** { *; }


-keep class org.apache.http.client.* { *; }
-keepclassmembers class org.apache.http.client.HttpClient.** {
  *;
}


-keep class org.apache.http.** { *; }
-keep class android.net.http.AndroidHttpClient { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient


# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn android.databinding.Bindable
-dontwarn android.databinding.BindingMethod
-dontwarn android.databinding.InverseBindingMethod
-dontwarn com.sun.activation.registries.LogSupport
-dontwarn com.sun.activation.registries.MailcapFile
-dontwarn com.sun.activation.registries.MimeTypeFile
-dontwarn java.awt.Component
-dontwarn java.awt.Graphics2D
-dontwarn java.awt.Graphics
-dontwarn java.awt.Image
-dontwarn java.awt.MediaTracker
-dontwarn java.awt.datatransfer.DataFlavor
-dontwarn java.awt.datatransfer.Transferable
-dontwarn java.awt.image.BufferedImage
-dontwarn java.awt.image.ImageObserver
-dontwarn java.awt.image.RenderedImage
-dontwarn java.beans.Introspector
-dontwarn javax.imageio.ImageIO
-dontwarn javax.imageio.ImageWriter
-dontwarn javax.imageio.stream.ImageOutputStream
-dontwarn javax.lang.model.SourceVersion
-dontwarn javax.lang.model.element.AnnotationMirror
-dontwarn javax.lang.model.element.AnnotationValue
-dontwarn javax.lang.model.element.AnnotationValueVisitor
-dontwarn javax.lang.model.element.Element
-dontwarn javax.lang.model.element.ElementKind
-dontwarn javax.lang.model.element.ElementVisitor
-dontwarn javax.lang.model.element.ExecutableElement
-dontwarn javax.lang.model.element.Name
-dontwarn javax.lang.model.element.PackageElement
-dontwarn javax.lang.model.element.TypeElement
-dontwarn javax.lang.model.element.VariableElement
-dontwarn javax.lang.model.type.ArrayType
-dontwarn javax.lang.model.type.DeclaredType
-dontwarn javax.lang.model.type.ExecutableType
-dontwarn javax.lang.model.type.IntersectionType
-dontwarn javax.lang.model.type.NoType
-dontwarn javax.lang.model.type.PrimitiveType
-dontwarn javax.lang.model.type.TypeKind
-dontwarn javax.lang.model.type.TypeMirror
-dontwarn javax.lang.model.type.TypeVariable
-dontwarn javax.lang.model.type.TypeVisitor
-dontwarn javax.lang.model.type.UnionType
-dontwarn javax.lang.model.type.WildcardType
-dontwarn javax.lang.model.util.AbstractElementVisitor6
-dontwarn javax.lang.model.util.ElementFilter
-dontwarn javax.lang.model.util.Elements
-dontwarn javax.lang.model.util.SimpleAnnotationValueVisitor6
-dontwarn javax.lang.model.util.SimpleAnnotationValueVisitor7
-dontwarn javax.lang.model.util.SimpleElementVisitor6
-dontwarn javax.lang.model.util.SimpleTypeVisitor6
-dontwarn javax.lang.model.util.SimpleTypeVisitor7
-dontwarn javax.lang.model.util.Types
-dontwarn javax.servlet.ServletContextListener
-dontwarn javax.tools.Diagnostic$Kind
-dontwarn javax.tools.JavaFileObject
-dontwarn javax.tools.SimpleJavaFileObject
-dontwarn org.apache.avalon.framework.logger.Logger
-dontwarn org.apache.log.Hierarchy
-dontwarn org.apache.log.Logger
-dontwarn org.apache.log4j.Level
-dontwarn org.apache.log4j.Logger
-dontwarn org.apache.log4j.Priority
-dontwarn org.jspecify.nullness.Nullable