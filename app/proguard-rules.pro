#指定代码的压缩级别
-optimizationpasses 5
#包名不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#优化  不优化输入的类文件
-dontoptimize
#预校验
-dontpreverify
#混淆时是否记录日志
-verbose
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*
#忽略警告
-ignorewarning
-dontwarn
#如果引用了v4或者v7包
-dontwarn android.support.**
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
-keep public class * implements java.io.Serializable {
       public *;
}

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    static transient <fields>;
    private <fields>;
    private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

####################### 项目特殊处理
#eventbus处理
-keepclassmembers class ** {
   public void onEvent*(**);
}

# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
   <init>(java.lang.Throwable);
}

# tencent
-keep class com.tencent.** { *; }
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
## shareSDK
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn **.R$*
-keep class m.framework.**{*;}

### umeng统计
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep public class com.ypwl.xiaotouzi.R$*{
   public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
### umeng分享与授权
-dontshrink
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**

-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**

-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}

-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep public class com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.umeng.soexample.R$*{
    public static final int *;
}
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keep class com.linkedin.** { *; }

### umeng消息推送
-keep class com.umeng.message.protobuffer.* {
        public <fields>;
        public <methods>;
}
-keep class com.squareup.wire.* {
        public <fields>;
        public <methods>;
}
-keep class com.umeng.message.local.* {
        public <fields>;
        public <methods>;
}
-keep class org.android.agoo.impl.*{
        public <fields>;
        public <methods>;
}
-keep class org.android.agoo.service.* {*;}
-keep class org.android.spdy.**{*;}
-keep public class com.ypwl.xiaotouzi.R$*{
    public static final int *;
}

# 保持下面的api类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends com.ypwl.xiaozouzi.manager.**{*;}
-keep public class * extends com.ypwl.xiaotouzi.view.expandablelayout.**{*;}

# 自定义的javabean
-keep class com.ypwl.xiaotouzi.bean.** { *; }

# 自定义view
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
# fastjson
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.alibaba.fastjson.**
# keep 泛型
-keepattributes Signature

#google的gson
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.uuhelper.Application.** { *; }

-keep class net.sourceforge.zbar.** { *; }
-keep class com.google.android.gms.** { *; }

###-----------MPAndroidChart图库相关的混淆配置------------
-keep class com.github.mikephil.charting.** { *; }

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}


####---------  reservoir 相关的混淆配置-------
#-keep class com.anupcowkur.reservoir.** { *;}
####-------- pulltorefresh 相关的混淆配置---------
#-dontwarn com.handmark.pulltorefresh.library.**
#-keep class com.handmark.pulltorefresh.library.** { *;}
#-dontwarn com.handmark.pulltorefresh.library.extras.**
#-keep class com.handmark.pulltorefresh.library.extras.** { *;}
#-dontwarn com.handmark.pulltorefresh.library.internal.**
#-keep class com.handmark.pulltorefresh.library.internal.** { *;}


###双向通信-推送和即时通信框架
-keep class com.ypwl.xiaotouzi.im.*{
        public <fields>;
        public <methods>;
}

#### otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

###  ijkmediaplayer视频框架
-keep class tv.danmaku.ijk.media.player.** {*;}
-dontwarn tv.danmaku.ijk.media.player.**
-keepclassmembers class * implements IMediaPlayer.OnPreparedListener {
    public <fields>;
    public <methods>;
}

##### ButterKnife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

### 高德定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
##3D 地图
-keep class com.amap.api.mapcore.**{*;}
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.*{*;}
##搜索
-keep class com.amap.api.services.**{*;}
#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

#tecent bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}