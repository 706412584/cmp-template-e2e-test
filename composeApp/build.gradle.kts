import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
}

// 本机验证 wasm 用：nodejs.org / github.com 在本网络不可达，Node/yarn 发行版走镜像。
// 云端 Actions 访问官方无障碍——正式模板不含这段。
plugins.withType<NodeJsPlugin> {
    (extensions.getByName("nodeJsSpec") as org.jetbrains.kotlin.gradle.targets.js.EnvSpec<*>)
        .downloadBaseUrl.set("https://npmmirror.com/mirrors/node")
}
plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
    val yarnRoot = extensions.getByName("yarnRootExtension")
    // downloadBaseUrlProperty 在 BaseYarnRootExtension 上是 internal，反射调用
    yarnRoot.javaClass.getMethod(
        "getDownloadBaseUrlProperty\$\$kotlin_gradle_plugin_common".replace("\$\$", "$"),
    ).let { m ->
        @Suppress("UNCHECKED_CAST")
        val prop = m.invoke(yarnRoot) as org.gradle.api.provider.Property<String>
        prop.set("https://npmmirror.com/mirrors/yarn")
    }
}
// binaryen（wasm 优化器，从 github.com releases 下载）同样走 npmmirror。
// extension 名是 kotlinWasmBinaryenSpec（project extensions 列表里确认），
// 它也是 EnvSpec 子类——直接强转拿公开的 downloadBaseUrl。
afterEvaluate {
    val spec = extensions.findByName("kotlinWasmBinaryenSpec") ?: return@afterEvaluate
    runCatching {
        (spec as org.jetbrains.kotlin.gradle.targets.js.EnvSpec<*>)
            .downloadBaseUrl.set("https://github.com/WebAssembly/binaryen/releases/download")
    }.onFailure { println("[cmp-local] binaryen mirror set failed: ${it.message}") }
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "app.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
        }
    }
}

android {
    namespace = "com.example.mycomposemultiplatform.shared"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
