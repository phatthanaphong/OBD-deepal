# EV Diagnostic — Android APK Project

## วิธี Build APK

### ต้องการ
- Android Studio (ฟรี) หรือ Command Line Tools
- Java 17+ / JDK

### ขั้นตอน (Android Studio — ง่ายที่สุด)

1. **เปิด Android Studio**
2. **File → Open** → เลือกโฟลเดอร์ `ev_apk_project`
3. รอ Gradle sync เสร็จ (~2-3 นาที)
4. **Build → Build Bundle(s)/APK(s) → Build APK(s)**
5. APK จะอยู่ที่: `app/build/outputs/apk/debug/app-debug.apk`

### ติดตั้งบนมือถือ
- เปิด Settings → Security → **Unknown Sources** (เปิด)
- Copy APK ไปมือถือ แล้วเปิดไฟล์ติดตั้ง
- หรือใช้ `adb install app-debug.apk`

### ฟีเจอร์ของแอป
- หน้า Home เลือกได้ 2 โหมด: Grid และ Flipbook
- ไม่มี Address Bar — เป็น Native App เต็มจอ
- ปุ่ม ✕ ออกจากแอปได้
- Source code ถูกซ่อนอยู่ใน APK (ต้อง decompile ถึงจะดูได้)
- รองรับ Android 5.0 (API 21) ขึ้นไป

## โครงสร้างไฟล์
```
ev_apk_project/
├── app/
│   ├── src/main/
│   │   ├── assets/
│   │   │   ├── ev_diagnostic_web.html      ← โหมด Grid
│   │   │   └── ev_diagnostic_flipbook.html ← โหมด Flipbook
│   │   ├── java/com/evdiag/app/
│   │   │   ├── SplashActivity.java   ← หน้าแรก
│   │   │   ├── MainActivity.java     ← WebView Grid
│   │   │   ├── FlipbookActivity.java ← WebView Flipbook
│   │   │   └── AppInterface.java     ← JS Bridge (ปุ่มออก)
│   │   └── res/
│   └── build.gradle
└── README.md
```
