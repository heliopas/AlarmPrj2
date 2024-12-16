// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

repositories {
    // <other repositories that might already exist, we will leave as is>
    // paho repository
    maven {
        url = uri("https://repo.eclipse.org/content/repositories/paho-snapshots/")
    }
}