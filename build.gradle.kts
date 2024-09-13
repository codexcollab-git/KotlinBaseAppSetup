// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
  repositories {
    google()
    mavenCentral()
    maven (url = "https://plugins.gradle.org/m2/")
  }
}
plugins {
  alias(libs.plugins.androidApplication) apply false
  alias(libs.plugins.jetbrainsKotlinAndroid) apply false
  alias(libs.plugins.ktlint) apply false
  alias(libs.plugins.kt.ksp) apply false
  alias(libs.plugins.kt.parcelize) apply false
  alias(libs.plugins.google.services) apply false
  alias(libs.plugins.hilt) apply false
}

