# MyComposeMultiplatform

由 **Android Code Studio** 的 *Compose Multiplatform* 模板生成：
一份共享的 Compose UI 代码，跑在 Android、Desktop（JVM）、Web（WASM）
三个平台上。

## 项目结构

```
composeApp/                    共享代码（KMP 模块）
└── src/commonMain/kotlin/
    └── com.example.mycomposemultiplatform.shared/App.kt    共享 Compose 界面
app/                           Android 壳（设备上构建出 APK）
desktop/                       Desktop 壳（云端构建出 fat jar）
.github/workflows/             推送到 GitHub 自动出包
```

## 各平台怎么构建

| 平台 | 在哪构建 | 命令 | 产物 |
|---|---|---|---|
| Android | **设备上（本 IDE）** | 构建按钮 / `./gradlew :app:assembleDebug` | APK |
| Desktop | GitHub Actions | 推送自动触发 | fat jar（`java -jar` 可跑） |
| Web | GitHub Actions | 推送自动触发 | 静态站点（可部署到任意静态托管） |
| iOS | GitHub Actions | 模板未启用，见 workflow 注释 | 模拟器二进制 |

## 改界面

只改 `composeApp/src/commonMain/kotlin/com.example.mycomposemultiplatform.shared/App.kt`
——三个平台共用这一份代码。

平台专属代码放各自源集：`composeApp/src/androidMain/`、
`composeApp/src/desktopMain/` 或 `composeApp/src/wasmJsMain/`。

## 版本组合

Compose Multiplatform 1.10.3 / Kotlin 2.2.20 /
AGP 8.13.0 / Gradle 8.13。

升级 CMP 前先查它的 aar-metadata 要求（1.12+ 需要 compileSdk 37 / AGP 9.1+，
会与设备端构建冲突）。
