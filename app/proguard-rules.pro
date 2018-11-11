# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------
#-app
-keep class com.gloiot.hygo.bean.** {*;}
-keep class com.gloiot.hygo.server.db.entity.** {*;}
-keep class com.gloiot.hygo.ui.activity.shopping.Bean.** {*;}
#-chatsdk
-keep class com.gloiot.chatsdk.bean.** {*;}
-keep public class * extends android.database.sqlite.SQLiteOpenHelper{*;}


#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------

#-ButterKnife 7.0
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
@butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
@butterknife.* <methods>;
}

#-picasso
-keep class com.squareup.picasso.** {*; }
-dontwarn com.squareup.picasso.**

#-greenDao混淆
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-dontwarn org.greenrobot.greendao.database.**
-dontwarn rx.**

#-recyclerview-swipe
-keepclasseswithmembers class android.support.v7.widget.RecyclerView$ViewHolder {
   public final View *;
}
#---添加了上述规则后还是存在问题，那么再追加：
#-dontwarn com.yanzhenjie.recyclerview.swipe.**
#-keep class com.yanzhenjie.recyclerview.swipe.** {*;}

#-bugly
-keep public class com.tencent.bugly.**{*;}

#地区3级联动选择器（citypickerview）
-keep class com.lljjcoder.**{
*;
}

#-pinyin4j
#-libraryjars libs/pinyin4j-2.5.0.jar
-dontwarn net.soureceforge.pinyin4j.**
-dontwarn demo.**
-keep class net.sourceforge.pinyin4j.** { *;}
-keep class demo.** { *;}

#-nineoldandroids动画lib包
#-libraryjars libs/nineoldandroids-2.4.0.jar
-keep class com.nineoldandroids.** { *; }
-keep interface com.nineoldandroids.** { *; }
-dontwarn com.nineoldandroids.**

#-org.bouncycastle.jar
#-libraryjars libs/org.bouncycastle.jar
-dontwarn org.bouncycastle.**
-keep class org.bouncycastle.**{*;}

#-okhttp-3.4.1.jar
#-libraryjars libs/okhttp-3.4.1.jar
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

#-aliyun-oss-sdk-android-2.2.0.jar
#-libraryjars libs/aliyun-oss-sdk-android-2.2.0.jar
-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn org.apache.commons.codec.binary.**
-keep class org.apache.** { *;}
#---打包出的的话将上面一句注释掉，改用下面的：
#-keep public class org.apache.commons.codec.binary.Base64 { *;}


#-okio-1.6.0.jar
#-libraryjars libs/okio-1.6.0.jar
-dontwarn okio.**

#-android-async-http-1.4.9.jar
#-libraryjars libs/android-async-http-1.4.4.jar
-dontwarn android-async-http-1.4.4.jar.**
-keep class android-async-http-1.4.4.jar.**{*;}
#-dontwarn com.loopj.android.http.**
#-keep class com.loopj.android.http.**{*;}

#-httpclient-4.3.6.jar
#-libraryjars libs/httpclient-4.3.6.jar
-keep class cz.msebera.android.httpclient.** {*; }

#-org.apache.http.legacy.jar
#-libraryjars libs/org.apache.http.legacy.jar

#-zxing.jar
#-libraryjars libs/zxing.jar
-keep class com.google.zxing.** {*;}
-dontwarn com.google.zxing.**

#-银联支付(UPPayAssistEx.jar/UPPayPluginExPro.jar)
-keep class org.simalliance.openmobileapi.** {*;}
-keep class org.simalliance.openmobileapi.service.** {*;}
-keep class com.unionpay.** {*;}

#-gson:2.7
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.examples.android.model.** { *; }

#-libammsdk.jar
#-libraryjars libs/libammsdk.jar

#-wechat-sdk-android-without-mta:1.0.2--微信支付
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** {*;}

#-alipaySdk-20160516.jar
#-libraryjars libs/alipaySdk-20160516.jar
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}

#-glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#-banner的混淆代码
-keep class com.youth.banner.** {
    *;
}

#-BaseRecyclerViewAdapterHelper的混淆代码
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}




#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------



#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------
-keep public class com.zyd.wlwsdk.utils.DatePrickerUtil {*;}



#----------------------------------------------------------------------------

#---------------------------------5.自定义View------------------------


#-------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------


#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
#指定压缩级别
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#忽略警告
-ignorewarning
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------