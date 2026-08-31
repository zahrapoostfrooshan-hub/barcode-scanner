# بارکدخوان (Barcode & QR Code Scanner)

اپلیکیشن اندروید بومی (Native Android) برای اسکن بارکد و QR Code، ساخته‌شده با:

- **Kotlin + Jetpack Compose** برای رابط کاربری فارسی و راست‌چین (RTL)
- **CameraX** برای دسترسی سریع و پایدار به دوربین
- **ML Kit Barcode Scanning** (مدل باندل‌شده، بدون نیاز به اینترنت) برای تشخیص بارکد/QR
- **Room** برای ذخیره‌سازی دائمی تاریخچه اسکن‌ها روی دستگاه

## ساختار پروژه

```
app/src/main/java/com/example/barcodescanner/
├── MainActivity.kt          # نقطه ورود و مسیریابی (Navigation)
├── BarcodeScannerApp.kt     # کلاس Application
├── data/                    # Room: Entity، Dao، Database، Repository
├── scanner/                 # تحلیل‌گر فریم دوربین با ML Kit
├── util/                    # نگاشت نوع بارکد به نام فارسی
├── viewmodel/               # ScanViewModel
└── ui/
    ├── screens/              # HomeScreen، ScanScreen، ResultScreen، HistoryScreen
    └── theme/                # رنگ، تایپوگرافی، تم Material3
```

## پیش‌نیازها

- Android Studio (Koala یا جدیدتر) یا Gradle 8.7 + JDK 17
- Android SDK با `compileSdk = 34`، `minSdk = 24`

## اجرای پروژه (روی امولاتور یا دستگاه متصل)

```bash
./gradlew installDebug
```

یا پروژه را در Android Studio باز کرده و روی Run بزنید.

## ساخت فایل APK

نسخه دیباگ (برای تست سریع):

```bash
./gradlew assembleDebug
```

خروجی در مسیر زیر قرار می‌گیرد:
`app/build/outputs/apk/debug/app-debug.apk`

نسخه ریلیز (بدون امضا؛ برای انتشار باید امضا شود):

```bash
./gradlew assembleRelease
```

خروجی در مسیر زیر قرار می‌گیرد:
`app/build/outputs/apk/release/app-release-unsigned.apk`

> برای انتشار در Google Play یا نصب عمومی، باید APK ریلیز را با یک keystore امضا کنید
> (`./gradlew assembleRelease` را بعد از تنظیم `signingConfigs` در `app/build.gradle.kts` اجرا کنید).

## نکات فنی

- تشخیص بارکد کاملاً **آفلاین** انجام می‌شود؛ کتابخانه `com.google.mlkit:barcode-scanning`
  مدل خود را در داخل APK باندل می‌کند و نیازی به دانلود یا اینترنت ندارد.
- فرمت‌های پشتیبانی‌شده: QR Code، EAN-13، EAN-8، UPC-A، UPC-E، Code 128، Code 39، ITF و چند فرمت دیگر.
- تاریخچه با Room در پایگاه‌داده SQLite محلی ذخیره می‌شود و بعد از بستن برنامه باقی می‌ماند.
- در اولین ورود به صفحه اسکن، مجوز دوربین درخواست می‌شود؛ در صورت رد شدن، پیام راهنما نمایش داده می‌شود.
