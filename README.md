# ThirdPay
Alipay and WeChat payment demo

![ThirdPay](https://github.com/BoBoMEe/ThirdPay/raw/master/Screenshot/screenshot.png "ThirdPay")

在使用微信支付的时候需要 先将constants里面的相关常量替换.
微信支付需要签名release包.

- add app.keystore to app module


- edit build.gradle in module 

 ``` 
 defaultConfig {
         applicationId "com.yaodu.drug"
         minSdkVersion 15
         targetSdkVersion 22
         versionCode 1
         versionName "1.0"
     }
  //签名
     signingConfigs {
         debug {
         }
         relealse {
 //            storeFile file('app.keystore')
 //            storePassword '***'
 //            keyAlias 'app'
 //            keyPassword '***'
         }
     }
     // 打包
     buildTypes {
         debug {
             buildConfigField "int", "ENV", "2"
             minifyEnabled false
             zipAlignEnabled false
             shrinkResources false
             proguardFile 'proguard-rules.pro'
         }
     }
 ```


- edit the manifest
``` xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.yaodu.drug">
```

