import com.danielefavaro.jetpackcomposeplayground.BuildType

plugins {
    alias(libs.plugins.jetpackcomposeplayground.android.application)
    alias(libs.plugins.jetpackcomposeplayground.android.application.compose)
//    alias(libs.plugins.jetpackcomposeplayground.android.hilt)
}

android {
    defaultConfig {
        applicationId = "com.danielefavaro.jetpackcomposeplayground"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        // Custom test runner to set up Hilt dependency graph
//        testInstrumentationRunner = "com.danielefavaro.jetpackcomposeplayground.core.testing.TestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = BuildType.DEBUG.applicationIdSuffix
        }
        val release by getting {
            isMinifyEnabled = true
            applicationIdSuffix = BuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    namespace = "com.danielefavaro.jetpackcomposeplayground"
}

dependencies {
    implementation(projects.core.ui)

    implementation(libs.core.ktx)
    implementation(libs.activity.compose)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.material3)

    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
//    implementation(libs.compose.runtime.tracing)
}
