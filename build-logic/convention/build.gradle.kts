import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.danielefavaro.jetpackcomposeplayground.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "jetpackcomposeplayground.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "jetpackcomposeplayground.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "jetpackcomposeplayground.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "jetpackcomposeplayground.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
//        register("androidHilt") {
//            id = "jetpackcomposeplayground.android.hilt"
//            implementationClass = "AndroidHiltConventionPlugin"
//        }
        register("jvmLibrary") {
            id = "jetpackcomposeplayground.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
