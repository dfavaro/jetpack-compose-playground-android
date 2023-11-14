package com.danielefavaro.jetpackcomposeplayground

/**
 * This can be shared between :app and any other module to provide configurations type safety.
 */
enum class BuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
