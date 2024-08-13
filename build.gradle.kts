// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.daggerHiltAndroid) apply false
//    id("androidx.navigation:navigation-safe-args-gradle-plugin") version "2.7.7" apply false

    alias(libs.plugins.navigationSafeArgs) apply false
}